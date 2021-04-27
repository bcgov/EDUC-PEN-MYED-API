package ca.bc.gov.educ.api.pen.myed.rest;

import ca.bc.gov.educ.api.pen.myed.exception.BusinessError;
import ca.bc.gov.educ.api.pen.myed.exception.BusinessException;
import ca.bc.gov.educ.api.pen.myed.exception.MyEdAPIRuntimeException;
import ca.bc.gov.educ.api.pen.myed.properties.ApplicationProperties;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.School;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class is used for REST calls
 *
 * @author Om
 */
@Component
@Slf4j
public class RestUtils {

  private static final String CONTENT_TYPE = "Content-Type";
  private final Executor bgTask = new EnhancedQueueExecutor.Builder()
    .setThreadFactory(new ThreadFactoryBuilder().setNameFormat("bg-task-executor-%d").build())
    .setCorePoolSize(1).setMaximumPoolSize(1).setKeepAliveTime(Duration.ofSeconds(60)).build();
  private final ReadWriteLock schoolLock = new ReentrantReadWriteLock();
  @Getter
  private final Map<String, School> schoolMap = new HashMap<>();
  /**
   * The Props.
   */

  private final ApplicationProperties props;
  private final WebClient webClient;
  @Value("${initialization.background.enabled}")
  private Boolean isBackgroundInitializationEnabled;

  /**
   * Instantiates a new Rest utils.
   *
   * @param props the props
   */
  @Autowired
  public RestUtils(final ApplicationProperties props, final WebClient webClient) {
    this.props = props;
    this.webClient = webClient;
  }


  @PostConstruct
  public void init() {
    if (this.isBackgroundInitializationEnabled != null && this.isBackgroundInitializationEnabled) {
      this.bgTask.execute(this::populateSchoolMap);
    } else {
      this.populateSchoolMap();
    }
  }

  @Scheduled(cron = "${schedule.jobs.load.school.cron}") // 0 0 0/4* * * every 4 hours
  public void scheduled() {
    final Lock writeLock = this.schoolLock.writeLock();
    try {
      writeLock.lock();
      this.init();
    } finally {
      writeLock.unlock();
    }
  }

  private void populateSchoolMap() {
    for (val school : this.getSchools()) {
      schoolMap.putIfAbsent(school.getDistNo() + school.getSchlNo(), school);
    }
  }

  public Optional<String> postBatchSubmission(final PenRequestBatch penRequestBatch) throws BusinessException {
    try {
      return Optional.ofNullable(this.webClient.post()
        .uri(this.props.getPenRegBatchApiUrl(), uriBuilder -> uriBuilder.path("/pen-request-batch-submission").build())
        .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(penRequestBatch), PenRequestBatch.class)
        .retrieve()
        .bodyToMono(String.class)
        .block());
    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.CONFLICT) {
        throw new BusinessException(BusinessError.BATCH_ALREADY_SUBMITTED, penRequestBatch.getSubmissionNumber());
      }
      throw new MyEdAPIRuntimeException(e);
    }
  }

  public List<School> getSchools() {
    return this.webClient.get()
      .uri(this.props.getSchoolApiUrl())
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .retrieve()
      .bodyToFlux(School.class)
      .collectList()
      .block();
  }

  public Optional<ResponseEntity<PenRequestBatchSubmissionResult>> findBatchSubmissionResult(final String batchID) {
    try{
      return Optional.ofNullable(this.webClient.get()
        .uri(this.props.getPenRegBatchApiUrl(), uriBuilder -> uriBuilder.path("/pen-request-batch-submission/{batchID}/result").build(batchID))
        .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .toEntity(PenRequestBatchSubmissionResult.class)
        .block());
    }catch (final WebClientResponseException ex) {
      if (ex.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
        log.info("no record found for :: {}", batchID);
        return Optional.of(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
      } else {
        throw ex;
      }
    }

  }

  public Optional<PenCoordinator> getPenCoordinator(final String mincode) {
    try {
      final var response = this.webClient.get()
        .uri(this.props.getSchoolApiUrl(), uri -> uri.path("/{mincode}/pen-coordinator").build(mincode))
        .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(PenCoordinator.class)
        .block();
      log.info("record found for :: {}", mincode);
      return Optional.ofNullable(response);
    } catch (final WebClientResponseException ex) {
      if (ex.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
        log.info("no record found for :: {}", mincode);
        return Optional.empty();
      } else {
        throw ex;
      }
    }
  }

}
