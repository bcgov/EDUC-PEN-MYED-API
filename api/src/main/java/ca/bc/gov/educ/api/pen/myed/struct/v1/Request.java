package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
public class Request extends PenRequestBatchSubmissionStudent {
  @Size(max = 8, min = 8)
  @NotNull
  String mincode;

  @Size(max = 32)
  @NotNull
  String createUser;

  @Size(max = 32)
  @NotNull
  String updateUser;
}
