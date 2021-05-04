package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * The type Pen request batch submission.
 */
@Data
public class PenRequestBatchSubmission {
  /**
   * The Mincode.
   */
  @Size(min = 8, max = 8)
  @NotNull
  String mincode;
  /**
   * The Submission number.
   */
  @Size(min = 8, max = 8)
  @NotBlank
  String submissionNumber;
  /**
   * The Vendor name.
   */
  @Size(max = 100)
  @NotBlank
  String vendorName;
  /**
   * The Product name.
   */
  @Size(max = 100)
  @NotBlank
  String productName;
  /**
   * The Product id.
   */
  @Size(max = 15)
  @NotBlank
  String productID;
  /**
   * The Student count.
   */
  @Size(max = 6)
  @NotNull
  String studentCount;

  /**
   * The School name.
   */
  @NotBlank
  String schoolName;
  /**
   * The Create user.
   */
  @NotBlank
  String createUser;
  /**
   * The Update user.
   */
  @NotBlank
  String updateUser;

  /**
   * The Students.
   */
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @NotEmpty
  @Valid
  List<PenRequestBatchSubmissionStudent> students;
}


