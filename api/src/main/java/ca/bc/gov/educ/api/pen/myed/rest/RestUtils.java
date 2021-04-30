package ca.bc.gov.educ.api.pen.myed.rest;

import ca.bc.gov.educ.api.pen.myed.properties.ApplicationProperties;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penmatch.PenMatchStudent;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penservices.PenRequestStudentValidationIssue;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penservices.PenRequestStudentValidationPayload;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.School;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.Student;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
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
  private final Map<String, School> schoolMap = new ConcurrentHashMap<>();
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
    val writeLock = this.schoolLock.writeLock();
    try {
      writeLock.lock();
      this.init();
    } finally {
      writeLock.unlock();
    }
  }

  private void populateSchoolMap() {
    for (val school : this.getSchools()) {
      this.schoolMap.putIfAbsent(school.getDistNo() + school.getSchlNo(), school);
    }
    log.info("loaded  {} schools to memory", this.schoolMap.values().size());
  }

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


  public Mono<ResponseEntity<PenCoordinator>> getPenCoordinator(final String mincode) {
    log.info("making api call to get pen coordinator data for :: {}", mincode);
    return this.webClient.get()
      .uri(this.props.getSchoolApiUrl(), uri -> uri.path("/{mincode}/pen-coordinator").build(mincode))
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.OK)) {
          log.info("API call to get Pen coordinator success :: {}, for mincode :: {}", response.rawStatusCode(), mincode);
          return response.toEntity(PenCoordinator.class);
        } else {
          log.info("API call to get Pen coordinator failed :: {}, for mincode :: {}", response.rawStatusCode(), mincode);
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        }
      });
  }

  public Mono<ResponseEntity<List<PenRequestStudentValidationIssue>>> validatePenRequestPayload(final PenRequestStudentValidationPayload validationPayload) {
    return this.webClient.post()
      .uri(this.props.getPenServicesApiURL(), uriBuilder -> uriBuilder.path("/validation/student-request").build())
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body(Mono.just(validationPayload), PenRequestStudentValidationPayload.class)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.OK)) {
          log.info("API call to Pen Services success :: {}", response.rawStatusCode());
          return response.toEntity(new ParameterizedTypeReference<List<PenRequestStudentValidationIssue>>() {
          });
        } else {
          log.info("API call to Pen Services failed :: {}", response.rawStatusCode());
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        }
      });
  }


  public Mono<ResponseEntity<PenMatchResult>> processPenMatch(final PenMatchStudent penMatchPayload) {
    return this.webClient.post()
      .uri(this.props.getPenMatchApiURL())
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body(Mono.just(penMatchPayload), PenMatchStudent.class)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.OK)) {
          return response.toEntity(PenMatchResult.class);
        } else {
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        }
      }).doOnSuccess(this::logPenMatchOutcome);
  }

  private void logPenMatchOutcome(ResponseEntity<PenMatchResult> penMatchResultResponseEntity) {
    if (penMatchResultResponseEntity.getStatusCodeValue() == 200) {
      val body = penMatchResultResponseEntity.getBody();
      log.info("Pen Match API call success, status :: {}, body :: {}", penMatchResultResponseEntity.getStatusCodeValue(), body != null ? body.toString() : "");
    } else {
      log.info("Pen Match API call failed, status :: {}", penMatchResultResponseEntity.getStatusCodeValue());
    }
  }

  public String getNextPenNumber() {
    val guid =  UUID.randomUUID();
    log.info("generate new pen called for guid :: {}",guid);
    val pen = this.webClient.get()
      .uri(this.props.getPenServicesApiURL(), uri -> uri.path("/next-pen-number")
        .queryParam("transactionID", guid).build())
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .retrieve()
      .bodyToMono(String.class)
      .block();
    log.info("got new pen :: {} for guid :: {}", pen, guid);
    return pen;
  }

  public Student createStudent(final Student student) {
    return this.webClient.post()
      .uri(this.props.getStudentApiURL())
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body(Mono.just(student), Student.class)
      .retrieve()
      .bodyToMono(Student.class)
      .block();
  }
}
