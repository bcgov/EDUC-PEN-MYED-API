package ca.bc.gov.educ.api.pen.myed.exception;

import lombok.val;

/**
 * The type Invalid parameter exception.
 */
public class InvalidParameterException extends RuntimeException {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 3909412893704324490L;

  /**
   * Instantiates a new Invalid parameter exception.
   *
   * @param searchParamsMap the search params map
   */
  public InvalidParameterException(String... searchParamsMap) {
        super(InvalidParameterException.generateMessage(searchParamsMap));
    }

  /**
   * Generate message string.
   *
   * @param searchParams the search params
   * @return the string
   */
  private static String generateMessage(String... searchParams) {
        val message = new StringBuilder("Unexpected request parameters provided: ");
        var prefix = "";
        for (String parameter:searchParams) {
            message.append(prefix);
            prefix = ",";
            message.append(parameter);
        }
        return message.toString();
    }
}
