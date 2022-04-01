package ca.bc.gov.educ.api.pen.myed.service.v1;

import ca.bc.gov.educ.api.pen.myed.exception.MyEdAPIRuntimeException;
import ca.bc.gov.educ.api.pen.myed.mappers.v1.MyEdStudentMapper;
import ca.bc.gov.educ.api.pen.myed.rest.RestUtils;
import ca.bc.gov.educ.api.pen.myed.struct.v1.MyEdStudent;
import ca.bc.gov.educ.api.pen.myed.struct.v1.MyEdSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ca.bc.gov.educ.api.pen.myed.util.JsonUtil.getJsonStringFromObject;

/**
 * The type Pen my ed service.
 */
@Service
@Slf4j
public class PenMyEdService {
  /**
   * The Rest utils.
   */
  private final RestUtils restUtils;

  /**
   * Instantiates a new Pen my ed service.
   *
   * @param restUtils the rest utils
   */
  @Autowired
  public PenMyEdService(final RestUtils restUtils) {
    this.restUtils = restUtils;
  }

  /**
   * Post batch submission mono.
   *
   * @param penRequestBatch the pen request batch
   * @return the mono
   */
  public Mono<ResponseEntity<String>> postBatchSubmission(final PenRequestBatch penRequestBatch) {
    return this.restUtils.postBatchSubmission(penRequestBatch);
  }

  /**
   * Gets batch submission result.
   *
   * @param batchSubmissionID the batch submission id
   * @return the batch submission result
   */
  public Mono<ResponseEntity<MyEdSubmissionResult>> getBatchSubmissionResult(final UUID batchSubmissionID) {
    return this.restUtils.findBatchSubmissionResult(batchSubmissionID.toString());
  }


  public Mono<ResponseEntity<PenRequestResult>> postPenRequestToBatchAPI(final Request request) {
    return this.restUtils.postPenRequestToBatchAPI(request).map(penRequestResultResponseEntity -> {
      val body = penRequestResultResponseEntity.getBody();
      if (body != null) {
        body.setBirthDate(RegExUtils.removeAll(body.getBirthDate(), "[^\\d]"));
        return ResponseEntity.status(penRequestResultResponseEntity.getStatusCode()).body(body);
      }
      return penRequestResultResponseEntity;
    });
  }

  public Mono<ResponseEntity<List<PenCoordinator>>> getPenCoordinators() {
    return this.restUtils.getPenCoordinators();
  }

  public Mono<ResponseEntity<List<MyEdStudent>>> findStudents(final List<String> penList) {
    try {
      val batches = Lists.partition(penList,1000);
      List<Mono<ResponseEntity<RestPageImpl<Student>>>> monos = new ArrayList<>();
      for(val batch : batches) {
        SearchCriteria criteria = SearchCriteria.builder().key("pen").operation(FilterOperation.IN).value(String.join(",", batch)).valueType(ValueType.STRING).build();
        List<SearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(criteria);
        List<Search> searches = new LinkedList<>();
        searches.add(Search.builder().searchCriteriaList(criteriaList).build());
        monos.add(this.restUtils.findStudentsByCriteria(getJsonStringFromObject(searches), batch.size()));
      }
      return Mono.zip(monos, this::mapResponse).map(ResponseEntity::ok);
    } catch (Exception e) {
      throw new MyEdAPIRuntimeException("Error while fetching students: " + e.getMessage());
    }
  }

  private List<MyEdStudent> mapResponse(Object [] objects) {
    List<MyEdStudent> students = new ArrayList<>();
    for(Object object : objects) {
      ResponseEntity<RestPageImpl<Student>> responseEntity = (ResponseEntity<RestPageImpl<Student>>) object;
      RestPageImpl<Student> studentRestPage = responseEntity.getBody();
      if(studentRestPage != null) {
        for(val student : studentRestPage.getContent()) {
          students.add(MyEdStudentMapper.mapper.toMyEdStudent(student));
        }
      }
    }
    return students;
  }
}
