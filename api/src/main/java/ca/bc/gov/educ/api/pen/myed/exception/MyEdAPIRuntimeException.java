package ca.bc.gov.educ.api.pen.myed.exception;

/**
 * The type Pen reg api runtime exception.
 */
public class MyEdAPIRuntimeException extends RuntimeException {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 5241655513745148898L;

  /**
   * Instantiates a new Pen reg api runtime exception.
   *
   * @param message the message
   */
  public MyEdAPIRuntimeException(String message) {
		super(message);
	}

  public MyEdAPIRuntimeException(Throwable exception) {
    super(exception);
  }

}
