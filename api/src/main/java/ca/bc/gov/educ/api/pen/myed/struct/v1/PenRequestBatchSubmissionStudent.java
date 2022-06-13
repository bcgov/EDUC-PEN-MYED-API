package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The type Pen request batch submission student.
 */
@Data
public class PenRequestBatchSubmissionStudent {
  /**
   * The Local student id.
   */
  @Size(max = 12)
  @NotBlank
  String localStudentID;
  /**
   * The Pen.
   */
  @Size(max = 10)
  String pen;
  /**
   * The Legal surname.
   */
  @Size(max = 25)
  @NotBlank
  String legalSurname;
  /**
   * The Legal given name.
   */
  @Size(max = 25)
  @NotBlank
  String legalGivenName;
  /**
   * The Legal middle name.
   */
  @Size(max = 25)
  String legalMiddleName;
  /**
   * The Usual surname.
   */
  @Size(max = 25)
  String usualSurname;
  /**
   * The Usual given name.
   */
  @Size(max = 25)
  String usualGivenName;
  /**
   * The Usual middle name.
   */
  @Size(max = 25)
  String usualMiddleName;
  /**
   * The Birth date.
   */
  @Size(max = 8, min = 8)
  @NotBlank
  String birthDate;
  /**
   * The Gender.
   */
  @NotBlank
  @Size(max = 1, min = 1)
  @Pattern(regexp = "[MFUX]")
  String gender;
  /**
   * The Enrolled grade code.
   */
  @NotBlank
  @Size(max = 2, min = 2)
  String enrolledGradeCode;
  /**
   * The Postal code.
   */
  @Size(max = 6, min = 6)
  String postalCode;
}
