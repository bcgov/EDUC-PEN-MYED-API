package ca.bc.gov.educ.api.pen.myed.validator;

import ca.bc.gov.educ.api.pen.myed.rest.RestUtils;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmission;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.school.School;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Pen my ed payload validator.
 */
@Component
@Slf4j
public class PenMyEdPayloadValidator {
  /**
   * The constant MINCODE.
   */
  private static final String MINCODE = "mincode";
  /**
   * The Rest utils.
   */
  private final RestUtils restUtils;
  /**
   * The Dob format.
   */
  private final DateTimeFormatter dobFormat = DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT);

  /**
   * Instantiates a new Pen my ed payload validator.
   *
   * @param restUtils the rest utils
   */
  public PenMyEdPayloadValidator(final RestUtils restUtils) {
    this.restUtils = restUtils;
  }

  /**
   * Validate batch submission payload list.
   *
   * @param penRequestBatchSubmission the pen request batch submission
   * @return the list
   */
  public List<FieldError> validateBatchSubmissionPayload(PenRequestBatchSubmission penRequestBatchSubmission) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    val school = validateSchoolInfo(penRequestBatchSubmission.getMincode(), apiValidationErrors);
    if (school != null && !penRequestBatchSubmission.getSchoolName().equalsIgnoreCase(school.getSchoolName())) {
      apiValidationErrors.add(createFieldError("schoolName", penRequestBatchSubmission.getSchoolName(), "School Name mismatch."));
    }
    if (!StringUtils.isNumeric(penRequestBatchSubmission.getMincode())) {
      apiValidationErrors.add(createFieldError(MINCODE, penRequestBatchSubmission.getMincode(), "mincode should be numeric."));
    }
    if (!StringUtils.isNumeric(penRequestBatchSubmission.getStudentCount())) {
      apiValidationErrors.add(createFieldError("studentCount", penRequestBatchSubmission.getStudentCount(), "studentCount should be numeric."));
    }
    var index = 0;

    for (val student : penRequestBatchSubmission.getStudents()) {
      try {
        LocalDate.parse(student.getBirthDate(), dobFormat);
      } catch (final DateTimeParseException e) {
        apiValidationErrors.add(createFieldError("students[" + index + "].birthdate", student.getBirthDate(), "invalid birth date , must be yyyyMMdd."));
      }
      index++;
    }
    return apiValidationErrors;
  }

  /**
   * Validate pen request payload list.
   *
   * @param request the request
   * @return the list
   */
  public List<FieldError> validatePenRequestPayload(final Request request) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    validateSchoolInfo(request.getMincode(), apiValidationErrors);
    try {
      LocalDate.parse(request.getBirthDate(), dobFormat);
    } catch (final DateTimeParseException e) {
      apiValidationErrors.add(createFieldError("birthdate", request.getBirthDate(), "invalid birth date , must be yyyyMMdd."));
    }
    return apiValidationErrors;
  }

  /**
   * Validate school info school.
   *
   * @param mincode             the mincode
   * @param apiValidationErrors the api validation errors
   * @return the school
   */
  private School validateSchoolInfo(String mincode, List<FieldError> apiValidationErrors) {
    val school = this.restUtils.getSchoolMap().get(mincode);
    if (school == null) {
      apiValidationErrors.add(createFieldError(MINCODE, mincode, "Invalid mincode."));
    } else {
      final String openedDate = school.getDateOpened();
      final String closedDate = school.getDateClosed();
      try {
        if (openedDate == null || LocalDate.parse(openedDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME).isAfter(LocalDate.now()) || (closedDate != null && LocalDate.parse(closedDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME).isBefore(LocalDate.now()))) {
          apiValidationErrors.add(createFieldError(MINCODE, mincode, "School is closed."));
        }
      } catch (final DateTimeParseException exception) {
        apiValidationErrors.add(createFieldError(MINCODE, mincode, "School is closed."));
      }
    }
    return school;
  }

  /**
   * Create field error field error.
   *
   * @param fieldName     the field name
   * @param rejectedValue the rejected value
   * @param message       the message
   * @return the field error
   */
  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("penRequest", fieldName, rejectedValue, false, null, null, message);
  }
}
