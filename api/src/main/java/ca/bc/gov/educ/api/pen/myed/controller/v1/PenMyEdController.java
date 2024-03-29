package ca.bc.gov.educ.api.pen.myed.controller.v1;

import ca.bc.gov.educ.api.pen.myed.endpoint.v1.PenMyEdApiEndpoint;
import ca.bc.gov.educ.api.pen.myed.exception.AcceptedException;
import ca.bc.gov.educ.api.pen.myed.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.pen.myed.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.pen.myed.exception.errors.ApiError;
import ca.bc.gov.educ.api.pen.myed.mappers.v1.PenRegBatchMapper;
import ca.bc.gov.educ.api.pen.myed.service.v1.PenMyEdService;
import ca.bc.gov.educ.api.pen.myed.struct.v1.*;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.StudentRegistrationContact;
import ca.bc.gov.educ.api.pen.myed.validator.PenMyEdPayloadValidator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * The type Pen my ed controller.
 */
@RestController
@Slf4j
public class PenMyEdController implements PenMyEdApiEndpoint {
  public static final String PAYLOAD_CONTAINS_INVALID_DATA = "Payload contains invalid data.";
  public static final String INVALID_BATCH_ID = "Invalid batch ID.";
  public static final String STILL_IN_PROGRESS = "Batch is still being processed.";
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
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message(PAYLOAD_CONTAINS_INVALID_DATA).status(BAD_REQUEST).build();
      error.addValidationErrors(payloadErrors);
      throw new InvalidPayloadException(error);
    }
    val penRequestBatch = PenRegBatchMapper.mapper.toPenRequestBatch(penRequestBatchSubmission);
    return this.penMyEdService.postBatchSubmission(penRequestBatch);
  }

  @Override
  public Mono<ResponseEntity<MyEdSubmissionResult>> batchSubmissionResult(final UUID batchSubmissionID) {
    try {
      return this.penMyEdService.getBatchSubmissionResult(batchSubmissionID);
    } catch (final EntityNotFoundException e) {
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message(INVALID_BATCH_ID).status(BAD_REQUEST).build();
      throw new InvalidPayloadException(error);
    } catch (final AcceptedException e) {
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message(STILL_IN_PROGRESS).status(ACCEPTED).build();
      throw new InvalidPayloadException(error);
    }
  }

  @Override
  public List<StudentRegistrationContact> getStudentRegistrationContacts() {
    return this.penMyEdService.getStudentRegistrationContacts();
  }

  @Override
  public Mono<ResponseEntity<PenRequestResult>> penRequest(final Request request) {
    // struct validation
    val payloadErrors = this.penMyEdPayloadValidator.validatePenRequestPayload(request);
    if (!payloadErrors.isEmpty()) {
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message(PAYLOAD_CONTAINS_INVALID_DATA).status(BAD_REQUEST).build();
      error.addValidationErrors(payloadErrors);
      throw new InvalidPayloadException(error);
    }
    return this.penMyEdService.postPenRequestToBatchAPI(request);
  }

  @Override
  public Mono<ResponseEntity<List<MyEdStudent>>> findStudents(final List<String> penList) {
    val payloadErrors = this.penMyEdPayloadValidator.validatePenList(penList);
    if (!payloadErrors.isEmpty()) {
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message(PAYLOAD_CONTAINS_INVALID_DATA).status(BAD_REQUEST).build();
      error.addValidationErrors(payloadErrors);
      throw new InvalidPayloadException(error);
    }
    return this.penMyEdService.findStudents(penList);
  }

}
