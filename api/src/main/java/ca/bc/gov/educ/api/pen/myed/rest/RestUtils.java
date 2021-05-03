package ca.bc.gov.educ.api.pen.myed.rest;

import ca.bc.gov.educ.api.pen.myed.properties.ApplicationProperties;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The type Rest utils.
 */
@Component
@Slf4j
public class RestUtils {

  /**
   * The constant CONTENT_TYPE.
   */
  private static final String CONTENT_TYPE = "Content-Type";
  /**
   * The Bg task.
   */
  private final Executor bgTask = new EnhancedQueueExecutor.Builder()
    .setThreadFactory(new ThreadFactoryBuilder().setNameFormat("bg-task-executor-%d").build())
    .setCorePoolSize(1).setMaximumPoolSize(1).setKeepAliveTime(Duration.ofSeconds(60)).build();
  /**
   * The School lock.
   */
  private final ReadWriteLock schoolLock = new ReentrantReadWriteLock();
  /**
   * The School map.
   */
  @Getter
  private final Map<String, School> schoolMap = new ConcurrentHashMap<>();

  /**
   * The Props.
   */

  private final ApplicationProperties props;
  /**
   * The Web client.
   */
  private final WebClient webClient;
  /**
   * The Is background initialization enabled.
   */
  @Value("${initialization.background.enabled}")
  private Boolean isBackgroundInitializationEnabled;

  /**
   * Instantiates a new Rest utils.
   *
   * @param props     the props
   * @param webClient the web client
   */
  @Autowired
  public RestUtils(final ApplicationProperties props, final WebClient webClient) {
    this.props = props;
    this.webClient = webClient;
  }


  /**
   * Init.
   */
  @PostConstruct
  public void init() {
    if (this.isBackgroundInitializationEnabled != null && this.isBackgroundInitializationEnabled) {
      this.bgTask.execute(() -> {
        this.populateSchoolMap();
        this.getPenCoordinators().block();
      });
    } else {
      this.populateSchoolMap();
      this.getPenCoordinators().block();
    }
  }

  /**
   * Scheduled.
   */
  @Scheduled(cron = "${schedule.jobs.load.school.cron}")
  public void scheduled() {
    val writeLock = this.schoolLock.writeLock();
    try {
      writeLock.lock();
      this.init();
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Populate school map.
   */
  private void populateSchoolMap() {
    for (val school : this.getSchools()) {
      this.schoolMap.putIfAbsent(school.getDistNo() + school.getSchlNo(), school);
    }
    log.info("loaded  {} schools to memory", this.schoolMap.values().size());
  }

  /**
   * Post batch submission mono.
   *
   * @param penRequestBatch the pen request batch
   * @return the mono
   */
  public Mono<ResponseEntity<String>> postBatchSubmission(final PenRequestBatch penRequestBatch) {
    return this.webClient.post()
      .uri(this.props.getPenRegBatchApiUrl(), uriBuilder -> uriBuilder.path("/pen-request-batch-submission").build())
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body(Mono.just(penRequestBatch), PenRequestBatch.class)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.CREATED)) {
          log.info("Batch submission POST API call success, status :: {}, submission Number :: {}", response.rawStatusCode(), penRequestBatch.getSubmissionNumber());
          return response.toEntity(String.class);
        } else {
          log.info("Batch submission POST API call failed, status :: {}, submission Number :: {}", response.rawStatusCode(), penRequestBatch.getSubmissionNumber());
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        }
      });
  }

  /**
   * Gets schools.
   *
   * @return the schools
   */
  public List<School> getSchools() {
    log.info("calling school api to load schools to memory");
    return this.webClient.get()
      .uri(this.props.getSchoolApiUrl())
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .retrieve()
      .bodyToFlux(School.class)
      .collectList()
      .block();
  }

  /**
   * Find batch submission result mono.
   *
   * @param batchID the batch id
   * @return the mono
   */
  public Mono<ResponseEntity<PenRequestBatchSubmissionResult>> findBatchSubmissionResult(final String batchID) {
    return this.webClient.get()
      .uri(this.props.getPenRegBatchApiUrl(), uriBuilder -> uriBuilder.path("/pen-request-batch-submission/{batchID}/result").build(batchID))
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.OK)) {
          log.info("Batch submission result API call success, status :: {}, batch ID :: {}", response.rawStatusCode(), batchID);
          return response.toEntity(PenRequestBatchSubmissionResult.class);
        } else {
          log.info("Batch submission result API call failed, status :: {}, batch ID :: {}", response.rawStatusCode(), batchID);
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        }
      });

  }




  public Mono<ResponseEntity<PenRequestResult>> postPenRequestToBatchAPI(final Request request) {
    return this.webClient.post()
      .uri(this.props.getPenRegBatchApiUrl(), uriBuilder -> uriBuilder.path("/pen-request").build())
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body(Mono.just(request), Request.class)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.OK) || response.statusCode().equals(HttpStatus.CREATED)) {
          log.info("API call to postPenRequestToBatchAPI success :: {}, for surname :: {}", response.rawStatusCode(), request.getLegalSurname());
          return response.toEntity(PenRequestResult.class);
        } else if (response.statusCode().equals(HttpStatus.MULTIPLE_CHOICES)) {
          log.info("API call to postPenRequestToBatchAPI success :: {}, for surname :: {}", response.rawStatusCode(), request.getLegalSurname());
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        } else {
          log.error("API call to postPenRequestToBatchAPI failure :: {}, for surname :: {}", response.rawStatusCode(), request.getLegalSurname());
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        }
      });
  }

  public Mono<ResponseEntity<List<PenCoordinator>>> getPenCoordinators() {
    log.info("making api call to get pen coordinators data ");
    return this.webClient.get()
      .uri(this.props.getSchoolApiUrl() + "/pen-coordinator")
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.OK)) {
          log.info("API call to get Pen coordinators success :: {}", response.rawStatusCode());
          return response.toEntity(new ParameterizedTypeReference<List<PenCoordinator>>() {
          });
        } else {
          log.error("API call to get Pen coordinators failed :: {}", response.rawStatusCode());
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        }
      });
  }
}
