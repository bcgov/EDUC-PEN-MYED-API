package ca.bc.gov.educ.api.pen.myed.exception;

import lombok.val;

import java.util.Map;

/**
 * The type Invalid value exception.
 */
public class InvalidValueException extends RuntimeException {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 8926815015510650437L;

  /**
   * Instantiates a new Invalid value exception.
   *
   * @param paramsMap the params map
   */
  public InvalidValueException(String... paramsMap) {
        super(InvalidValueException.generateMessage(
            ExceptionUtils.toMap(String.class, String.class, (Object[]) paramsMap))) ;
    }

  /**
   * Generate message string.
   *
   * @param values the values
   * @return the string
   */
  private static String generateMessage(Map<String, String> values) {
        val message = "Invalid request parameters provided: ";
        return message + values;
    }
}
