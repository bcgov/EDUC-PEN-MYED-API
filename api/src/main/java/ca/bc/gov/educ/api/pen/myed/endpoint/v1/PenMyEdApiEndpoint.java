package ca.bc.gov.educ.api.pen.myed.endpoint.v1;

import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmission;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.PenCoordinator;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping("/api/v1/pen-myed")
@OpenAPIDefinition(info = @Info(title = "API for Pen MyEd Integration.", description = "This API exposes different endpoints for MyEd.", version = "1"), security =
  {@SecurityRequirement(name =
    "OAUTH2", scopes = {"MYED_READ_PEN_REQUEST_BATCH", "MYED_WRITE_PEN_REQUEST_BATCH", "MYED_READ_PEN_COORDINATOR","MYED_VALIDATE_PEN"})})
public interface PenMyEdApiEndpoint {
  @PostMapping("/pen-request-batch-submission")
  @PreAuthorize("hasAuthority('SCOPE_MYED_WRITE_PEN_REQUEST_BATCH')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST") , @ApiResponse(responseCode = "409", description = "CONFLICT")})
  @ResponseStatus(CREATED)
  @Tag(name = "Endpoint to create Pen Request Batch Submission.",
    description = "This endpoint will allow MyEd to submit a batch request via api call. If the api call was success it will return a guid {batchSubmissionID} for further tracking.")
  @Schema(name = "PenRequestBatchSubmission", implementation = PenRequestBatchSubmission.class)
  Mono<ResponseEntity<String>> createNewBatchSubmission(@Validated @RequestBody PenRequestBatchSubmission penRequestBatchSubmission);

  @GetMapping("/pen-request-batch-submission/{batchSubmissionID}/result")
  @PreAuthorize("hasAuthority('SCOPE_MYED_READ_PEN_REQUEST_BATCH')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK" , content = @Content(schema = @Schema(name = "PenRequestBatchSubmissionResult", implementation = PenRequestBatchSubmissionResult.class))), @ApiResponse(responseCode = "202", description = "ACCEPTED"), @ApiResponse(responseCode = "404", description =
    "NOT FOUND")})
  @Tag(name = "Endpoint to get Pen Request Batch Submission results.",
    description = "This endpoint will allow MyEd to query the results of a batch earlier submitted.")
  @Schema(name = "PenRequestBatchSubmissionResult", implementation = PenRequestBatchSubmissionResult.class)
  Mono<ResponseEntity<PenRequestBatchSubmissionResult>> batchSubmissionResult(@PathVariable UUID batchSubmissionID);

  @GetMapping("/{mincode}/pen-coordinator")
  @PreAuthorize("hasAuthority('SCOPE_MYED_READ_PEN_COORDINATOR')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(name = "PenCoordinator", implementation = PenCoordinator.class))),
    @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "Endpoint to get Pen Coordinator by Mincode.", description = "Endpoint to get Pen Coordinator by Mincode.")

  Mono<ResponseEntity<PenCoordinator>> getPenCoordinatorByMinCode(@PathVariable("mincode")  String mincode);

  @PostMapping("/validate-pen")
  @PreAuthorize("hasAuthority('SCOPE_MYED_VALIDATE_PEN')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Tag(name = "Endpoint to validate student data.", description = "Endpoint to validate student data.")
  @Schema(name = "PenValidation", implementation = Request.class)
  ResponseEntity<Boolean> validatePEN(@Validated @RequestBody Request request);

  @PostMapping("/pen-request")
  @PreAuthorize("hasAuthority('SCOPE_MYED_PEN_REQUEST')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "300", description = "Multiple Choices")})
  @Tag(name = "Endpoint to request a student PEN.", description = "Endpoint to request a student PEN.")
  @Schema(name = "Request", implementation = Request.class)
  Mono<ResponseEntity<PenRequestResult>> penRequest(@Validated @RequestBody Request request);
}
