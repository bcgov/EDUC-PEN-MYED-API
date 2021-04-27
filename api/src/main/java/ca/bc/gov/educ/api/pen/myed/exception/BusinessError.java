package ca.bc.gov.educ.api.pen.myed.exception;

import lombok.Getter;

/**
 * The enum Business error.
 */
public enum BusinessError {
  /**
   * The Event already persisted.
   */
  BATCH_ALREADY_SUBMITTED("Batch with submission number :: $? , is already submitted.");

  @Getter
  private final String code;

  BusinessError(String code) {
    this.code = code;

  }
}
