package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.mappers.LocalDateTimeMapper;
import ca.bc.gov.educ.api.pen.myed.mappers.UUIDMapper;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmission;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionStudent;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatchStudent;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface Pen reg batch mapper.
 */
@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
@DecoratedWith(PenRegBatchDecorator.class)
public interface PenRegBatchMapper {
  /**
   * The constant mapper.
   */
  PenRegBatchMapper mapper = Mappers.getMapper(PenRegBatchMapper.class);


  /**
   * To pen request batch pen request batch.
   *
   * @param penRequestBatchSubmission the pen request batch submission
   * @return the pen request batch
   */
  @Mapping(target = "sourceApplication", constant = "MyED")
  @Mapping(target = "sisVendorName", source = "vendorName")
  @Mapping(target = "sisProductName", source = "productName")
  @Mapping(target = "sisProductID", source = "productID")
  @Mapping(target = "penRequestBatchTypeCode", constant = "SCHOOL")
  @Mapping(target = "penRequestBatchStatusCode", constant = "LOADED")
  @Mapping(target = "penRequestBatchProcessTypeCode", constant = "API")
  @Mapping(target = "ministryPRBSourceCode", constant = "TSWPENWEB")
  @Mapping(target = "fileType", constant = "PEN")
  @Mapping(target = "insertDate", ignore = true)
  @Mapping(target = "fixableCount", ignore = true)
  @Mapping(target = "fileName", ignore = true)
  @Mapping(target = "extractDate", ignore = true)
  @Mapping(target = "errorCount", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "contactName", ignore = true)
  @Mapping(target = "matchedCount", ignore = true)
  @Mapping(target = "students", ignore = true)
  @Mapping(target = "studentCount", ignore = true)
  @Mapping(target = "sourceStudentCount", ignore = true)
  @Mapping(target = "searchedCount", ignore = true)
  @Mapping(target = "repeatCount", ignore = true)
  @Mapping(target = "processDate", ignore = true)
  @Mapping(target = "penRequestBatchStatusReason", ignore = true)
  @Mapping(target = "penRequestBatchID", ignore = true)
  @Mapping(target = "officeNumber", ignore = true)
  @Mapping(target = "newPenCount", ignore = true)
  PenRequestBatch toPenRequestBatch(PenRequestBatchSubmission penRequestBatchSubmission);

  /**
   * To pen request batch student pen request batch student.
   *
   * @param penRequestBatchSubmissionStudent the pen request batch submission student
   * @return the pen request batch student
   */
  @Mapping(target = "usualMiddleNames", source = "usualMiddleName")
  @Mapping(target = "usualLastName", source = "usualSurname")
  @Mapping(target = "usualFirstName", source = "usualGivenName")
  @Mapping(target = "legalMiddleNames", source = "legalMiddleName")
  @Mapping(target = "legalLastName", source = "legalSurname")
  @Mapping(target = "legalFirstName", source = "legalGivenName")
  @Mapping(target = "submittedPen", source = "pen")
  @Mapping(target = "gradeCode", source = "enrolledGradeCode")
  @Mapping(target = "genderCode", source = "gender")
  @Mapping(target = "dob", source = "birthDate")
  @Mapping(target = "penRequestBatchStudentStatusCode", constant = "LOADED")
  @Mapping(target = "localID", source = "localStudentID")
  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "submissionNumber", ignore = true)
  @Mapping(target = "studentID", ignore = true)
  @Mapping(target = "repeatRequestSequenceNumber", ignore = true)
  @Mapping(target = "repeatRequestOriginalID", ignore = true)
  @Mapping(target = "recordNumber", ignore = true)
  @Mapping(target = "questionableMatchStudentId", ignore = true)
  @Mapping(target = "penRequestBatchStudentID", ignore = true)
  @Mapping(target = "penRequestBatchID", ignore = true)
  @Mapping(target = "mincode", ignore = true)
  @Mapping(target = "matchAlgorithmStatusCode", ignore = true)
  @Mapping(target = "infoRequest", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "bestMatchPEN", ignore = true)
  @Mapping(target = "assignedPEN", ignore = true)
  PenRequestBatchStudent toPenRequestBatchStudent(PenRequestBatchSubmissionStudent penRequestBatchSubmissionStudent);
}
