package ca.bc.gov.educ.api.pen.myed.exception;

import lombok.Getter;

/**
 * The enum Business error.
 */
public enum BusinessError {
  /**
   * The Batch already submitted.
   */
  BATCH_ALREADY_SUBMITTED("Batch with submission number :: $? , is already submitted.");

  /**
   * The Code.
   */
  @Getter
  private final String code;

  /**
   * Instantiates a new Business error.
   *
   * @param code the code
   */
  BusinessError(String code) {
    this.code = code;

  }
}
