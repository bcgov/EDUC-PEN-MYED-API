package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penservices.PenRequestStudentValidationIssue;

import java.util.List;

@Data
public class ListItem {

  String mincode;
  String pen;
  String localID;
  String legalSurname;
  String legalGivenName;
  String legalMiddleNames;
  String usualSurname;
  String usualGivenName;
  String usualMiddleNames;
  String birthDate;
  String gender;
  String enrolledGradeCode;
  String postalCode;
  List<PenRequestStudentValidationIssue> validationIssues;
}
