package ca.bc.gov.educ.api.pen.myed.exception;

import ca.bc.gov.educ.api.pen.myed.exception.errors.ApiError;
import org.jboss.logging.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger log = Logger.getLogger(RestExceptionHandler.class);


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        log.info("handleEntityNotFound", ex);
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        log.error("{} ", apiError.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

    /**
     * Handles IllegalArgumentException
     *
     * @param ex the InvalidParameterException
     * @return the ApiError object
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleInvalidParameter(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        log.error("{} ",apiError.getMessage(), ex);
        return buildResponseEntity(apiError);
    }


    /**
     * Handles handleDateTimeParseException
     *
     * @param ex the DateTimeParseException
     * @return the ApiError object
     */
    @ExceptionHandler(DateTimeParseException.class)
    protected ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage().concat(" , Expected pattern is 'yyyy-mm-ddTHH:MM:SS' for date time field or 'yyyy-mm-dd' for date field."));
        log.error("{} ", apiError.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidPayloadException.class)
    protected ResponseEntity<Object> handleInvalidPayload(
            InvalidPayloadException ex) {
        log.error("", ex);
        return buildResponseEntity(ex.getError());
    }
}
