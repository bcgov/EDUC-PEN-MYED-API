package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The type Request.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Request extends PenRequestBatchSubmissionStudent {
  /**
   * The Mincode.
   */
  @Size(max = 8, min = 8)
  @NotNull
  String mincode;

  /**
   * The Create user.
   */
  @Size(max = 32)
  @NotNull
  String createUser;

  /**
   * The Update user.
   */
  @Size(max = 32)
  @NotNull
  String updateUser;
}
