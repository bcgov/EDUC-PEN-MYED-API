package ca.bc.gov.educ.api.pen.myed.exception;

import ca.bc.gov.educ.api.pen.myed.exception.errors.ApiError;
import lombok.val;
import org.jboss.logging.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * The type Rest exception handler.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * The constant log.
   */
  private static final Logger log = Logger.getLogger(RestExceptionHandler.class);


  /**
   * Build response entity response entity.
   *
   * @param apiError the api error
   * @return the response entity
   */
  private ResponseEntity<Object> buildResponseEntity(final ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  /**
   * Handle entity not found response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(
    final EntityNotFoundException ex) {
    log.info("handleEntityNotFound", ex);
    val apiError = new ApiError(NOT_FOUND);
    apiError.setMessage(ex.getMessage());
    log.error("{} ", apiError.getMessage(), ex);
    return this.buildResponseEntity(apiError);
  }

  /**
   * Handle invalid parameter response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<Object> handleInvalidParameter(final IllegalArgumentException ex) {
    val apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(ex.getMessage());
    log.error("{} ", apiError.getMessage(), ex);
    return this.buildResponseEntity(apiError);
  }


  /**
   * Handle date time parse exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(DateTimeParseException.class)
  protected ResponseEntity<Object> handleDateTimeParseException(final DateTimeParseException ex) {
    val apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(ex.getMessage().concat(" , Expected pattern is 'yyyy-mm-ddTHH:MM:SS' for date time field or 'yyyy-mm-dd' for date field."));
    log.error("{} ", apiError.getMessage(), ex);
    return this.buildResponseEntity(apiError);
  }

  /**
   * Handle invalid payload response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(InvalidPayloadException.class)
  protected ResponseEntity<Object> handleInvalidPayload(
    final InvalidPayloadException ex) {
    log.error("", ex);
    return this.buildResponseEntity(ex.getError());
  }

  /**
   * Handle invalid parameter response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler({InvalidParameterException.class, InvalidValueException.class})
  protected ResponseEntity<Object> handleInvalidParameter(final RuntimeException ex) {
    log.warn("handleInvalidParameter or InvalidValue", ex);
    val apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(ex.getMessage());
    log.error("{} ", apiError.getMessage(), ex);
    return this.buildResponseEntity(apiError);
  }

  /**
   * Handles MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
   *
   * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    final MethodArgumentNotValidException ex,
    final HttpHeaders headers,
    final HttpStatus status,
    final WebRequest request) {
    log.warn("handleMethodArgumentNotValid", ex);
    val apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage("Validation error");
    apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
    apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
    log.error("{} ", apiError.getMessage(), ex);
    return this.buildResponseEntity(apiError);
  }
}
