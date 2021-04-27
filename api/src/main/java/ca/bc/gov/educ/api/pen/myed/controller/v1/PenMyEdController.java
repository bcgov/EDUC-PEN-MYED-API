package ca.bc.gov.educ.api.pen.myed.controller.v1;

import ca.bc.gov.educ.api.pen.myed.endpoint.v1.PenMyEdApiEndpoint;
import ca.bc.gov.educ.api.pen.myed.exception.BusinessException;
import ca.bc.gov.educ.api.pen.myed.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.pen.myed.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.pen.myed.exception.errors.ApiError;
import ca.bc.gov.educ.api.pen.myed.mappers.v1.PenRegBatchMapper;
import ca.bc.gov.educ.api.pen.myed.service.v1.PenMyEdService;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmission;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import ca.bc.gov.educ.api.pen.myed.validator.PenRequestBatchSubmissionValidator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@Slf4j
public class PenMyEdController implements PenMyEdApiEndpoint {
  private final PenRequestBatchSubmissionValidator penRequestBatchSubmissionValidator;
  private final PenMyEdService penMyEdService;

  @Autowired
  public PenMyEdController(PenRequestBatchSubmissionValidator penRequestBatchSubmissionValidator, PenMyEdService penMyEdService) {
    this.penRequestBatchSubmissionValidator = penRequestBatchSubmissionValidator;
    this.penMyEdService = penMyEdService;
  }

  @Override
  public ResponseEntity<String> createNewBatchSubmission(PenRequestBatchSubmission penRequestBatchSubmission) {

    val payloadErrors = penRequestBatchSubmissionValidator.validatePayload(penRequestBatchSubmission);
    if (!payloadErrors.isEmpty()) {
      ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Payload contains invalid data.").status(BAD_REQUEST).build();
      error.addValidationErrors(payloadErrors);
      throw new InvalidPayloadException(error);
    }
    try {
      val penRequestBatch = PenRegBatchMapper.mapper.toPenRequestBatch(penRequestBatchSubmission);
      return ResponseEntity.status(HttpStatus.CREATED).body(this.penMyEdService.postBatchSubmission(penRequestBatch).orElseThrow());
    } catch (final BusinessException exception) {
      ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message(exception.getErrorMessage()).status(CONFLICT).build();
      error.addValidationErrors(payloadErrors);
      throw new InvalidPayloadException(error);
    }
  }

  @Override
  public ResponseEntity<PenRequestBatchSubmissionResult> batchSubmissionResult(UUID batchSubmissionID) {
    return this.penMyEdService.getBatchSubmissionResult(batchSubmissionID);
  }

  @Override
  public ResponseEntity<PenCoordinator> getPenCoordinatorByMinCode(String mincode) {
    val penCoordinator = this.penMyEdService.getPenCoordinatorByMinCode(mincode);
    if (penCoordinator.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(penCoordinator.get());
    }
    throw new EntityNotFoundException(PenCoordinator.class, mincode);
  }

  @Override
  public ResponseEntity<Boolean> validatePEN(Request request) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // TODO
  }

  @Override
  public ResponseEntity<PenRequestResult> penRequest(Request request) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // TODO
  }
}
