package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PenRequestBatchSubmissionStudent {
  @Size(max = 12)
  @NotBlank
  String localStudentID;
  @Size(max = 10)
  String pen;
  @Size(max = 25)
  @NotBlank
  String legalSurname;
  @Size(max = 25)
  @NotBlank
  String legalGivenName;
  @Size(max = 25)
  String legalMiddleName;
  @Size(max = 25)
  String usualSurname;
  @Size(max = 25)
  String usualGivenName;
  @Size(max = 25)
  String usualMiddleName;
  @Size(max = 8, min = 8)
  @NotBlank
  String birthDate;
  @NotBlank
  @Size(max = 1, min = 1)
  @Pattern(regexp = "[MFUX]")
  String gender;
  @NotBlank
  @Size(max = 2, min = 2)
  String enrolledGradeCode;
  @Size(max = 6, min = 6)
  @Pattern(regexp = "^([A-Z]\\d[A-Z]\\d[A-Z]\\d|)$")
  @NotBlank
  String postalCode;
}
