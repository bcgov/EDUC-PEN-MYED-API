package ca.bc.gov.educ.api.pen.myed.struct.v1.penservices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * The type Pen request batch student validation payload.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PenRequestStudentValidationPayload {

  /**
   * The Is interactive.
   */
  @NotNull
  Boolean isInteractive;
  /**
   * The Transaction id.
   */
  @NotNull
  String transactionID;
  /**
   * The Local id.
   */
  String localID;
  /**
   * The Submitted pen.
   */
  String submittedPen;
  /**
   * The Legal first name.
   */
  String legalFirstName;
  /**
   * The Legal middle names.
   */
  String legalMiddleNames;
  /**
   * The Legal last name.
   */
  String legalLastName;
  /**
   * The Usual first name.
   */
  String usualFirstName;
  /**
   * The Usual middle names.
   */
  String usualMiddleNames;
  /**
   * The Usual last name.
   */
  String usualLastName;
  /**
   * The Dob.
   */
  String dob;
  /**
   * The Gender code.
   */
  String genderCode;
  /**
   * The Grade code.
   */
  String gradeCode;
  /**
   * The Postal code.
   */
  String postalCode;
  /**
   * The Create user.
   */
  String createUser;
  /**
   * The Update user.
   */
  String updateUser;
  String mincode;
}
