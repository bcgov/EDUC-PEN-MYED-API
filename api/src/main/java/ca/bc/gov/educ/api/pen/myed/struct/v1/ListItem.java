package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penservices.PenRequestStudentValidationIssue;

import java.util.List;

/**
 * The type List item.
 */
@Data
public class ListItem {

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
  String legalMiddleNames;
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
  String usualMiddleNames;
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
