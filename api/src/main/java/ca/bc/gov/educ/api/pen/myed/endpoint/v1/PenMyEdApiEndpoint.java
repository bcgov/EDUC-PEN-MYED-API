package ca.bc.gov.educ.api.pen.myed.endpoint.v1;

import ca.bc.gov.educ.api.pen.myed.struct.v1.*;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.StudentRegistrationContact;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * The interface Pen my ed api endpoint.
 */
@RequestMapping("/api/v1/pen-myed")
@OpenAPIDefinition(info = @Info(title = "API for Pen MyEd Integration.", description = "This API exposes different endpoints for MyEd.", version = "1"), security =
  {@SecurityRequirement(name =
    "OAUTH2", scopes = {"MYED_READ_PEN_REQUEST_BATCH", "MYED_WRITE_PEN_REQUEST_BATCH", "MYED_READ_PEN_COORDINATOR", "MYED_VALIDATE_PEN"})})
public interface PenMyEdApiEndpoint {
  /**
   * Create new batch submission mono.
   *
   * @param penRequestBatchSubmission the pen request batch submission
   * @return the mono
   */
  @PostMapping("/pen-request-batch")
  @PreAuthorize("hasAuthority('SCOPE_MYED_WRITE_PEN_REQUEST_BATCH')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "409", description = "CONFLICT")})
  @ResponseStatus(CREATED)
  @Tag(name = "Endpoint to create Pen Request Batch Submission.",
    description = "This endpoint will allow MyEd to submit a batch request via api call. If the api call was success it will return a guid {batchSubmissionID} for further tracking.")
  @Schema(name = "PenRequestBatchSubmission", implementation = PenRequestBatchSubmission.class)
  Mono<ResponseEntity<String>> createNewBatchSubmission(@Validated @RequestBody PenRequestBatchSubmission penRequestBatchSubmission);

  /**
   * Batch submission result mono.
   *
   * @param batchSubmissionID the batch submission id
   * @return the mono
   */
  @GetMapping("/pen-request-batch/{batchSubmissionID}/result")
  @PreAuthorize("hasAuthority('SCOPE_MYED_READ_PEN_REQUEST_BATCH')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(name = "PenRequestBatchSubmissionResult", implementation = MyEdSubmissionResult.class))), @ApiResponse(responseCode = "202", description = "ACCEPTED"), @ApiResponse(responseCode = "404", description =
    "NOT FOUND")})
  @Tag(name = "Endpoint to get Pen Request Batch Submission results.",
    description = "This endpoint will allow MyEd to query the results of a batch earlier submitted.")
  @Schema(name = "MyEdSubmissionResult", implementation = MyEdSubmissionResult.class)
  Mono<ResponseEntity<MyEdSubmissionResult>> batchSubmissionResult(@PathVariable UUID batchSubmissionID);

  /**
   *
   */
  @GetMapping("/pen-coordinators")
  @PreAuthorize("hasAuthority('SCOPE_MYED_READ_PEN_COORDINATOR')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(name = "StudentRegistrationContact", implementation = StudentRegistrationContact.class)))),
    @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "Endpoint to get all student registration contacts.", description = "Endpoint to get all student registration contacts.")
  List<StudentRegistrationContact> getStudentRegistrationContacts();


  /**
   * Pen request mono.
   *
   * @param request the request
   * @return the mono
   */
  @PostMapping("/pen-request")
  @PreAuthorize("hasAuthority('SCOPE_MYED_PEN_REQUEST')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(name = "PenRequestResult", implementation = PenRequestResult.class))), @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(name = "PenRequestResult", implementation = PenRequestResult.class))), @ApiResponse(responseCode = "300", description = "Multiple Choices")})
  @Tag(name = "Endpoint to request a student PEN.", description = "Endpoint to request a student PEN.")
  @Schema(name = "Request", implementation = Request.class)
  Mono<ResponseEntity<PenRequestResult>> penRequest(@Validated @RequestBody Request request);

  /**
   * Pen request mono.
   *
   * @param penList the list of pen numbers for which student details will be fetched.
   * @return the mono
   */
  @PostMapping("/students")
  @PreAuthorize("hasAuthority('SCOPE_MYED_READ_STUDENT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(name = "Student", implementation = MyEdStudent.class)))), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to find student demographics by pen.", description = "This endpoint accepts a list of pen numbers in the payload as a json and responds the demographic information found for those pen numbers.")
  Mono<ResponseEntity<List<MyEdStudent>>> findStudents(@Validated @RequestBody List<String> penList);
}
