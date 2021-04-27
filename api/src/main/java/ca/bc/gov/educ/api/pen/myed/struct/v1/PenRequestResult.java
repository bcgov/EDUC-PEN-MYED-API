package ca.bc.gov.educ.api.pen.myed.struct.v1;

import ca.bc.gov.educ.api.pen.myed.struct.v1.penservices.PenRequestStudentValidationIssue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PenRequestResult {
  List<PenRequestStudentValidationIssue> validationIssues;
  Integer mincode;
  String localStudentID;
  String pen;
  String legalSurname;
  String legalGivenName;
  String legalMiddleName;
  String usualSurname;
  String usualGivenName;
  String usualMiddleName;
  String birthDate;
  String gender;
  String enrolledGradeCode;
  String postalCode;
}
