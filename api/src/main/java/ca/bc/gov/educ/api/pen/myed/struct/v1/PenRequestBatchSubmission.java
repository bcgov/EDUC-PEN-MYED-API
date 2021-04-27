package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
public class PenRequestBatchSubmission {
  @Size(min = 8, max = 8)
  @NotNull
  Integer mincode;
  @Size(min = 8, max = 8)
  @NotBlank
  String submissionNumber;
  @Size(max = 100)
  @NotBlank
  String vendorName;
  @Size(max = 100)
  @NotBlank
  String productName;
  @Size(max = 15)
  @NotBlank
  String productID;
  @Size(max = 6)
  @NotBlank
  Integer studentCount;
  @NotBlank
  String schoolGroupCode;
  @NotBlank
  String schoolName;

  @NotBlank
  String createUser;

  /**
   * The Update user.
   */
  @NotBlank
  String updateUser;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @NotEmpty
  List<PenRequestBatchSubmissionStudent> students;
}


