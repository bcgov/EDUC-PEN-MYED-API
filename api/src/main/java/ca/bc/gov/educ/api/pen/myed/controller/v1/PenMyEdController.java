package ca.bc.gov.educ.api.pen.myed.controller.v1;

import ca.bc.gov.educ.api.pen.myed.endpoint.v1.PenMyEdApiEndpoint;
import ca.bc.gov.educ.api.pen.myed.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.pen.myed.exception.errors.ApiError;
import ca.bc.gov.educ.api.pen.myed.mappers.v1.PenRegBatchMapper;
import ca.bc.gov.educ.api.pen.myed.service.v1.PenMyEdService;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmission;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import ca.bc.gov.educ.api.pen.myed.validator.PenMyEdPayloadValidator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * The type Pen my ed controller.
 */
@RestController
@Slf4j
public class PenMyEdController implements PenMyEdApiEndpoint {
  /**
   * The Pen my ed payload validator.
   */
  private final PenMyEdPayloadValidator penMyEdPayloadValidator;
  /**
   * The Pen my ed service.
   */
  private final PenMyEdService penMyEdService;

  /**
   * Instantiates a new Pen my ed controller.
   *
   * @param penMyEdPayloadValidator the pen my ed payload validator
   * @param penMyEdService          the pen my ed service
   */
  @Autowired
  public PenMyEdController(final PenMyEdPayloadValidator penMyEdPayloadValidator, final PenMyEdService penMyEdService) {
    this.penMyEdPayloadValidator = penMyEdPayloadValidator;
    this.penMyEdService = penMyEdService;
  }

  @Override
  public Mono<ResponseEntity<String>> createNewBatchSubmission(final PenRequestBatchSubmission penRequestBatchSubmission) {

    val payloadErrors = this.penMyEdPayloadValidator.validateBatchSubmissionPayload(penRequestBatchSubmission);
    if (!payloadErrors.isEmpty()) {
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Payload contains invalid data.").status(BAD_REQUEST).build();
      error.addValidationErrors(payloadErrors);
      throw new InvalidPayloadException(error);
    }
    val penRequestBatch = PenRegBatchMapper.mapper.toPenRequestBatch(penRequestBatchSubmission);
    return this.penMyEdService.postBatchSubmission(penRequestBatch);
  }

  @Override
  public Mono<ResponseEntity<PenRequestBatchSubmissionResult>> batchSubmissionResult(final UUID batchSubmissionID) {
    return this.penMyEdService.getBatchSubmissionResult(batchSubmissionID);
  }

  @Override
  public Mono<ResponseEntity<PenCoordinator>> getPenCoordinatorByMinCode(final String mincode) {
    return this.penMyEdService.getPenCoordinatorByMinCode(mincode);
  }

  @Override
  public ResponseEntity<Boolean> validatePEN(final Request request) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // TODO
  }

  @Override
  public Mono<ResponseEntity<PenRequestResult>> penRequest(final Request request) {
    // struct validation
    val payloadErrors = this.penMyEdPayloadValidator.validatePenRequestPayload(request);
    if (!payloadErrors.isEmpty()) {
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Payload contains invalid data.").status(BAD_REQUEST).build();
      error.addValidationErrors(payloadErrors);
      throw new InvalidPayloadException(error);
    }
    return this.penMyEdService.postPenRequestToBatchAPI(request);
  }

}
