package ca.bc.gov.educ.api.pen.myed.service.v1;

import ca.bc.gov.educ.api.pen.myed.rest.RestUtils;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penmatch.PenMatchStudent;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penservices.PenRequestStudentValidationIssue;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penservices.PenRequestStudentValidationPayload;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PenMyEdService {
  private final RestUtils restUtils;

  @Autowired
  public PenMyEdService(final RestUtils restUtils) {
    this.restUtils = restUtils;
  }

  public Mono<ResponseEntity<String>> postBatchSubmission(final PenRequestBatch penRequestBatch) {
    return this.restUtils.postBatchSubmission(penRequestBatch);
  }

  public Mono<ResponseEntity<PenRequestBatchSubmissionResult>> getBatchSubmissionResult(final UUID batchSubmissionID) {
    return this.restUtils.findBatchSubmissionResult(batchSubmissionID.toString());
  }

  public Mono<ResponseEntity<PenCoordinator>> getPenCoordinatorByMinCode(final String mincode) {
    return this.restUtils.getPenCoordinator(mincode);
  }

  public ResponseEntity<List<PenRequestStudentValidationIssue>> validatePenRequestPayload(final PenRequestStudentValidationPayload validationPayload) {
    return this.restUtils.validatePenRequestPayload(validationPayload).block();
  }

  public ResponseEntity<PenMatchResult> processPenMatch(final PenMatchStudent penMatchPayload) {
    return this.restUtils.processPenMatch(penMatchPayload).block();
  }

  public String getNextPenNumber() {
    return this.restUtils.getNextPenNumber();
  }

  public Student createStudent(final Student student) {
  return this.restUtils.createStudent(student);
  }
}
