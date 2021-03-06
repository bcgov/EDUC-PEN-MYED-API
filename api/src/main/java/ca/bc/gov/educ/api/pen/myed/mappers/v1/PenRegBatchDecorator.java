package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmission;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatchStudent;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Pen reg batch decorator.
 */
@Slf4j
public abstract class PenRegBatchDecorator implements PenRegBatchMapper {

  /**
   * The Mapper.
   */
  private final PenRegBatchMapper mapper;

  /**
   * Instantiates a new Pen reg batch decorator.
   *
   * @param mapper the mapper
   */
  protected PenRegBatchDecorator(final PenRegBatchMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public PenRequestBatch toPenRequestBatch(PenRequestBatchSubmission penRequestBatchSubmission) {
    List<PenRequestBatchStudent> penRequestBatchStudents = new ArrayList<>();
    val penRequestBatch = this.mapper.toPenRequestBatch(penRequestBatchSubmission);
    penRequestBatch.setStudentCount(String.valueOf(penRequestBatchSubmission.getStudents().size()));
    penRequestBatch.setSourceStudentCount(String.valueOf(penRequestBatchSubmission.getStudents().size()));
    penRequestBatch.setFileName(UUID.randomUUID().toString());
    penRequestBatch.setExtractDate(LocalDateTime.now().withNano(0).toString());
    penRequestBatch.setInsertDate(LocalDateTime.now().withNano(0).toString());
    if (penRequestBatch.getMincode().startsWith("102")) {
      penRequestBatch.setSchoolGroupCode("PSI");
    } else {
      penRequestBatch.setSchoolGroupCode("K12");
    }

    var recordNum = 1;
    for (val student : penRequestBatchSubmission.getStudents()) {
      final PenRequestBatchStudent penRequestBatchStudent = this.mapper.toPenRequestBatchStudent(student);
      updateStudent(penRequestBatchSubmission, penRequestBatchStudent);
      penRequestBatchStudent.setRecordNumber(recordNum++);
      penRequestBatchStudents.add(penRequestBatchStudent);
    }
    penRequestBatch.setStudents(penRequestBatchStudents);
    return penRequestBatch;
  }

  /**
   * Update student.
   *
   * @param penRequestBatchSubmission the pen request batch submission
   * @param penRequestBatchStudent    the pen request batch student
   */
  private void updateStudent(PenRequestBatchSubmission penRequestBatchSubmission, PenRequestBatchStudent penRequestBatchStudent) {
    penRequestBatchStudent.setCreateUser(penRequestBatchSubmission.getCreateUser());
    penRequestBatchStudent.setUpdateUser(penRequestBatchSubmission.getUpdateUser());
    penRequestBatchStudent.setMincode(String.valueOf(penRequestBatchSubmission.getMincode()));
    penRequestBatchStudent.setSubmissionNumber(penRequestBatchSubmission.getSubmissionNumber());
  }
}
