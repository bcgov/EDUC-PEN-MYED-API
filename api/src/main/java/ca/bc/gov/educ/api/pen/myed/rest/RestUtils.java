package ca.bc.gov.educ.api.pen.myed.rest;

import ca.bc.gov.educ.api.pen.myed.exception.AcceptedException;
import ca.bc.gov.educ.api.pen.myed.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.pen.myed.exception.MyEdAPIRuntimeException;
import ca.bc.gov.educ.api.pen.myed.mappers.v1.SubmissionResultMapper;
import ca.bc.gov.educ.api.pen.myed.properties.ApplicationProperties;
import ca.bc.gov.educ.api.pen.myed.struct.v1.MyEdSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.district.District;
import ca.bc.gov.educ.api.pen.myed.struct.v1.district.DistrictContact;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.School;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.SchoolContact;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.StudentRegistrationContact;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.RestPageImpl;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.Student;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
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

  @Getter
  private final Map<String, School> schoolMincodeMap = new ConcurrentHashMap<>();

  @Getter
  private final Map<String, School> schoolIDMap = new ConcurrentHashMap<>();

  @Getter
  private final Map<String, District> districtIDMap = new ConcurrentHashMap<>();

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
        this.populateSchoolMaps();
        this.populateDistrictMap();
      });
    } else {
      this.populateSchoolMaps();
      this.populateDistrictMap();
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

  private void populateSchoolMaps() {
    for (val school : this.getSchools()) {
      this.schoolMincodeMap.put(school.getMincode(), school);
      this.schoolIDMap.put(school.getSchoolId(), school);
    }
    log.info("Loaded  {} schools to memory", this.schoolIDMap.values().size());
  }

  private void populateDistrictMap() {
    for (val district : this.getDistricts()) {
      this.districtIDMap.put(district.getDistrictId(), district);
    }
    log.info("Loaded  {} districts to memory", this.districtIDMap.values().size());
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

  public List<District> getDistricts() {
    log.info("Calling Institute api to get list of districts");
    return this.webClient.get()
            .uri(this.props.getInstituteApiUrl() + "/district")
            .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToFlux(District.class)
            .collectList()
            .block();
  }

  public List<School> getSchools() {
    log.info("Calling Institute api to get list of schools");
    return this.webClient.get()
            .uri(this.props.getInstituteApiUrl() + "/school")
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
  public Mono<ResponseEntity<MyEdSubmissionResult>> findBatchSubmissionResult(final String batchID) {
    try {
      var response = this.webClient.get()
        .uri(this.props.getPenRegBatchApiUrl(), uriBuilder -> uriBuilder.path("/pen-request-batch-submission/{batchID}/result").build(batchID))
        .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .onStatus(
          HttpStatus.ACCEPTED::equals,
          responseAccepted -> {throw new AcceptedException();} )
        .bodyToMono(PenRequestBatchSubmissionResult.class)
        .block();

      if (response == null) {
        throw new MyEdAPIRuntimeException("Error while fetching batch submission result");
      }

      return Mono.just(ResponseEntity.ok(SubmissionResultMapper.mapper.toMyEdSubmissionResult(response)));
    }catch (final WebClientResponseException e) {
      if(e.getStatusCode() == HttpStatus.NOT_FOUND) {
        throw new EntityNotFoundException();
      }
      log.error("Error while fetching batch submission result", e);
      return null;
    }catch (final AcceptedException e) {
      throw new AcceptedException();
    }
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

  public List<StudentRegistrationContact> getStudentRegistrationContactList() {
    try {
      log.info("Calling Institute api to get list of school and district student registration contacts");
      List<SchoolContact> schoolContacts = this.webClient.get()
              .uri(this.props.getInstituteApiUrl()
                      + "/api/v1/institute/school/contact/paginated?pageNumber=0&pageSize=10000")
              .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .retrieve()
              .bodyToFlux(SchoolContact.class)
              .collectList()
              .block();

      List<DistrictContact> districtContacts = this.webClient.get()
              .uri(this.props.getInstituteApiUrl()
                      + "/api/v1/institute/district/contact/paginated?pageNumber=0&pageSize=10000&searchCriteriaList=[{'condition':null,'searchCriteriaList':[{'key':'districtContactTypeCode','operation':'eq','value':'STUDREGIS','valueType':'STRING','condition':'AND'}]}]")
              .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .retrieve()
              .bodyToFlux(DistrictContact.class)
              .collectList()
              .block();

      var schools = getSchoolIDMap();
      var districts = getDistrictIDMap();

      List<StudentRegistrationContact> studentRegistrationContacts = new ArrayList<>();
      schoolContacts.forEach(schoolContact -> {
        var school = schools.get(schoolContact.getSchoolId());
        StudentRegistrationContact coordinator = new StudentRegistrationContact();
        coordinator.setMincode(school.getMincode());
        coordinator.setDistrictNumber(school.getMincode().substring(0, 3));
        coordinator.setSchoolNumber(school.getMincode().substring(4));
        coordinator.setPenCoordinatorName(StringUtils.trim(schoolContact.getFirstName() + " " + schoolContact.getLastName()));
        coordinator.setPenCoordinatorEmail(schoolContact.getEmail());
        studentRegistrationContacts.add(coordinator);
      });

      districtContacts.forEach(districtContact -> {
        var district = districts.get(districtContact.getDistrictId());
        StudentRegistrationContact coordinator = new StudentRegistrationContact();
        coordinator.setMincode(district.getDistrictNumber() + "00000");
        coordinator.setDistrictNumber(district.getDistrictNumber());
        coordinator.setSchoolNumber("00000");
        coordinator.setPenCoordinatorName(StringUtils.trim(districtContact.getFirstName() + " " + districtContact.getLastName()));
        coordinator.setPenCoordinatorEmail(districtContact.getEmail());
        studentRegistrationContacts.add(coordinator);
      });
      return studentRegistrationContacts;
    }catch(Exception e){
      log.error("API call to Institute API failure getting student registration contacts :: {}", e.getMessage());
      throw new MyEdAPIRuntimeException("API call to Institute API failure getting student registration contacts, contact the Ministry for more info.");
    }
  }

  public Mono<ResponseEntity<RestPageImpl<Student>>> findStudentsByCriteria(final String criteriaJSON, final Integer pageSize) {
    return this.webClient.get()
      .uri(this.getStudentPaginatedURI(criteriaJSON, pageSize))
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.OK)) {
          log.info("API call to find students success :: {}", response.rawStatusCode());
          return response.toEntity(new ParameterizedTypeReference<RestPageImpl<Student>>() {
          });
        } else {
          log.error("API call to find students failed :: {}", response.rawStatusCode());
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        }
      });
  }

  private URI getStudentPaginatedURI(final String criteriaJSON, final Integer pageSize) {
    return UriComponentsBuilder.fromHttpUrl(this.props.getStudentApiURL())
      .path("/paginated")
      .queryParam("searchCriteriaList", criteriaJSON)
      .queryParam("pageSize", pageSize).build().encode().toUri();

  }
}
