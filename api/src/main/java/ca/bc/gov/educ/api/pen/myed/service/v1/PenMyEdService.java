package ca.bc.gov.educ.api.pen.myed.service.v1;

import ca.bc.gov.educ.api.pen.myed.exception.BusinessException;
import ca.bc.gov.educ.api.pen.myed.rest.RestUtils;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PenMyEdService {
  private final RestUtils restUtils;

  @Autowired
  public PenMyEdService(RestUtils restUtils) {
    this.restUtils = restUtils;
  }

  public Optional<String> postBatchSubmission(final PenRequestBatch penRequestBatch) throws BusinessException {
    return this.restUtils.postBatchSubmission(penRequestBatch);
  }

  public ResponseEntity<PenRequestBatchSubmissionResult> getBatchSubmissionResult(UUID batchSubmissionID) {
    return this.restUtils.findBatchSubmissionResult(batchSubmissionID.toString()).orElseThrow();
  }

  public Optional<PenCoordinator> getPenCoordinatorByMinCode(String mincode){
    return this.restUtils.getPenCoordinator(mincode);
  }
}
