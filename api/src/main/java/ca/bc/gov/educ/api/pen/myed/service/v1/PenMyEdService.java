package ca.bc.gov.educ.api.pen.myed.service.v1;

import ca.bc.gov.educ.api.pen.myed.rest.RestUtils;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

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
    return this.restUtils.postPenRequestToBatchAPI(request);
  }

  public Mono<ResponseEntity<List<PenCoordinator>>> getPenCoordinators() {
    return this.restUtils.getPenCoordinators();
  }
}
