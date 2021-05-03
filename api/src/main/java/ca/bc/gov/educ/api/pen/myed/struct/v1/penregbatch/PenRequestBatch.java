package ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch;

import lombok.*;

import java.util.List;

/**
 * The type Pen request batch.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PenRequestBatch {
  /**
   * The Pen request batch id.
   */
  String penRequestBatchID;
  /**
   * The Submission number.
   */
  String submissionNumber;
  /**
   * The Pen request batch status code.
   */
  String penRequestBatchStatusCode;
  /**
   * The Pen request batch status reason.
   */
  String penRequestBatchStatusReason;
  /**
   * The Pen request batch type code.
   */
  String penRequestBatchTypeCode;
  /**
   * The File name.
   */
  String fileName;
  /**
   * The File type.
   */
  String fileType;
  /**
   * The Insert date.
   */
  String insertDate;
  /**
   * The Extract date.
   */
  String extractDate;
  /**
   * The Process date.
   */
  String processDate;
  /**
   * The Source application.
   */
  String sourceApplication;
  /**
   * The Ministry prb source code.
   */
  String ministryPRBSourceCode;
  /**
   * The Mincode.
   */
  String mincode;
  /**
   * The School name.
   */
  String schoolName;
  /**
   * The Contact name.
   */
  String contactName;
  /**
   * The Email.
   */
  String email;
  /**
   * The Office number.
   */
  String officeNumber;
  /**
   * The Source student count.
   */
  String sourceStudentCount;
  /**
   * The Student count.
   */
  String studentCount;

  /**
   * The New pen count.
   */
  String newPenCount;
  /**
   * The Error count.
   */
  String errorCount;
  /**
   * The Matched count.
   */
  String matchedCount;
  /**
   * The Repeat count.
   */
  String repeatCount;
  /**
   * The Fixable count.
   */
  String fixableCount;
  /**
   * The Sis vendor name.
   */
  String sisVendorName;
  /**
   * The Sis product name.
   */
  String sisProductName;
  /**
   * The Sis product id.
   */
  String sisProductID;
  /**
   * The Pen request batch process type code.
   */
  String penRequestBatchProcessTypeCode;
  /**
   * The School group code.
   */
  String schoolGroupCode;

  /**
   * The Create user.
   */
  String createUser;

  /**
   * The Update user.
   */
  String updateUser;

  /**
   * The Searched count.
   */
  Long searchedCount;

  /**
   * The Students.
   */
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<PenRequestBatchStudent> students;
}
