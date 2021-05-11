package ca.bc.gov.educ.api.pen.myed.service.v1;

import ca.bc.gov.educ.api.pen.myed.mappers.v1.MyEdStudentMapper;
import ca.bc.gov.educ.api.pen.myed.rest.RestUtils;
import ca.bc.gov.educ.api.pen.myed.struct.v1.MyEdStudent;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.FilterOperation;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.Search;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.SearchCriteria;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.ValueType;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
  public Mono<ResponseEntity<PenRequestBatchSubmissionResult>> getBatchSubmissionResult(final UUID batchSubmissionID) {
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
    SearchCriteria criteria = SearchCriteria.builder().key("pen").operation(FilterOperation.IN).value(String.join(",", penList)).valueType(ValueType.STRING).build();
    List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(criteria);
    List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    return this.restUtils.findStudentsByCriteria(getJsonStringFromObject(searches), penList.size()).map(restPageResponseEntity -> {
      if (restPageResponseEntity.getStatusCode() == HttpStatus.OK) {
        val body = restPageResponseEntity.getBody();
        if (body != null && body.getContent() != null) {
          return ResponseEntity.status(restPageResponseEntity.getStatusCode()).body(body.getContent().stream().map(MyEdStudentMapper.mapper::toMyEdStudent).collect(Collectors.toList()));
        }
        return ResponseEntity.status(restPageResponseEntity.getStatusCode()).build();
      }
      return ResponseEntity.status(restPageResponseEntity.getStatusCode()).build();
    });
  }
}
