package ca.bc.gov.educ.api.pen.myed.controller.v1;

import ca.bc.gov.educ.api.pen.myed.constants.MatchAlgorithmStatusCode;
import ca.bc.gov.educ.api.pen.myed.endpoint.v1.PenMyEdApiEndpoint;
import ca.bc.gov.educ.api.pen.myed.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.pen.myed.exception.errors.ApiError;
import ca.bc.gov.educ.api.pen.myed.mappers.v1.PenMatchMapper;
import ca.bc.gov.educ.api.pen.myed.mappers.v1.PenMyEdMapper;
import ca.bc.gov.educ.api.pen.myed.mappers.v1.PenRegBatchMapper;
import ca.bc.gov.educ.api.pen.myed.mappers.v1.PenServicesMapper;
import ca.bc.gov.educ.api.pen.myed.service.v1.PenMyEdService;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmission;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import ca.bc.gov.educ.api.pen.myed.validator.PenMyEdPayloadValidator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Slf4j
public class PenMyEdController implements PenMyEdApiEndpoint {
  private final PenMyEdPayloadValidator penMyEdPayloadValidator;
  private final PenMyEdService penMyEdService;

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
    //rule validation
    val penValidationPayload = PenServicesMapper.mapper.toValidationPayload(request);
    val validationResponse = this.penMyEdService.validatePenRequestPayload(penValidationPayload);
    if (validationResponse.getStatusCode() != HttpStatus.OK) {
      return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
    if (validationResponse.getBody() != null && validationResponse.getBody().size() > 0) {
      final var isError = validationResponse.getBody().stream().anyMatch(x -> "ERROR".equals(x.getPenRequestBatchValidationIssueSeverityCode()));
      if (isError) {
        val response = PenMyEdMapper.mapper.toResult(request);
        response.setValidationIssues(validationResponse.getBody());
        return Mono.just(ResponseEntity.status(BAD_REQUEST).body(response));
      }
    }
    // call pen match api
    val penMatchPayload = PenMatchMapper.mapper.toPenMatchStudent(request);
    val penMatchResponse = this.penMyEdService.processPenMatch(penMatchPayload);
    return this.handlePenmatchResponse(penMatchResponse, request);

  }

  private Mono<ResponseEntity<PenRequestResult>> handlePenmatchResponse(final ResponseEntity<PenMatchResult> penMatchResponse, final Request request) {
    val penMatchResult = penMatchResponse.getBody();
    if (penMatchResponse.getStatusCode() != HttpStatus.OK || penMatchResult == null || penMatchResult.getPenStatus() == null) {
      log.error("Pen match result invalid :: {}", penMatchResult);
      return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
    val algorithmStatusCode = MatchAlgorithmStatusCode.valueOf(penMatchResult.getPenStatus());
    val penRequestResult = PenMyEdMapper.mapper.toResult(request);
    switch (algorithmStatusCode) {
      case AA:
      case B1:
      case C1:
      case D1:
        //sys match
        val firstStudent = penMatchResult.getMatchingRecords().stream().findFirst();
        if (firstStudent.isEmpty() || StringUtils.isBlank(firstStudent.get().getMatchingPEN())) {
          log.error("Pen match result does not contain matched student for algorithmStatusCode :: {}", algorithmStatusCode);
          return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
        penRequestResult.setPen(firstStudent.get().getMatchingPEN());
        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(penRequestResult));
      case B0:
      case C0:
      case D0:
        // new student
        val pen = this.penMyEdService.getNextPenNumber();
        val student = PenMyEdMapper.mapper.toStudent(request, pen);
        val createdStudent = this.penMyEdService.createStudent(student);
        penRequestResult.setPen(createdStudent.getPen());
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(penRequestResult));
      case F1:
      case BM:
      case CM:
      case DM:
      case G0:
      default:
        // multiple matches
        return Mono.just(ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES).build());
    }
  }

}
