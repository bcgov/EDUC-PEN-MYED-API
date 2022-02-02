package ca.bc.gov.educ.api.pen.myed.struct.v1;

import ca.bc.gov.educ.api.pen.myed.struct.v1.penservices.PenRequestStudentValidationIssue;
import lombok.Data;

import java.util.List;

/**
 * The type List item.
 */
@Data
public class MyEdListItem {

  /**
   * The Mincode.
   */
  String mincode;
  /**
   * The Pen.
   */
  String pen;
  /**
   * The Local id.
   */
  String localID;
  /**
   * The Legal surname.
   */
  String legalSurname;
  /**
   * The Legal given name.
   */
  String legalGivenName;
  /**
   * The Legal middle names.
   */
  String legalMiddleName;
  /**
   * The Usual surname.
   */
  String usualSurname;
  /**
   * The Usual given name.
   */
  String usualGivenName;
  /**
   * The Usual middle names.
   */
  String usualMiddleName;
  /**
   * The Birth date.
   */
  String birthDate;
  /**
   * The Gender.
   */
  String gender;
  /**
   * The Enrolled grade code.
   */
  String enrolledGradeCode;
  /**
   * The Postal code.
   */
  String postalCode;
  /**
   * The Validation issues.
   */
  List<PenRequestStudentValidationIssue> validationIssues;
}
