package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.mappers.LocalDateTimeMapper;
import ca.bc.gov.educ.api.pen.myed.mappers.UUIDMapper;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
public interface PenMyEdMapper {
  PenMyEdMapper mapper = Mappers.getMapper(PenMyEdMapper.class);
  @Mapping(target = "validationIssues", ignore = true)
  PenRequestResult toResult (Request request);

  @Mapping(target = "usualMiddleNames", source = "request.usualMiddleName")
  @Mapping(target = "usualLastName",  source = "request.usualMiddleName")
  @Mapping(target = "usualFirstName", source = "request.usualMiddleName")
  @Mapping(target = "trueStudentID", ignore = true)
  @Mapping(target = "studentID", ignore = true)
  @Mapping(target = "statusCode", constant="A")
  @Mapping(target = "sexCode",expression="java(ca.bc.gov.educ.api.pen.myed.util.CodeUtil.getSexCodeFromGenderCode(request.getGender()))")
  @Mapping(target = "memo", ignore = true)
  @Mapping(target = "localID",expression="java(ca.bc.gov.educ.api.pen.myed.util.LocalIDUtil.changeBadLocalID(request.getLocalStudentID()))")
  @Mapping(target = "legalMiddleNames", source = "request.legalMiddleName")
  @Mapping(target = "legalLastName", source = "request.legalSurname")
  @Mapping(target = "legalFirstName", source = "request.legalGivenName")
  @Mapping(target = "historyActivityCode", constant = "REQNEW")
  @Mapping(target = "gradeYear", ignore = true)
  @Mapping(target = "gradeCode", source = "request.enrolledGradeCode")
  @Mapping(target = "genderCode", source = "request.gender")
  @Mapping(target = "emailVerified", constant = "N")
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "dob", expression="java(ca.bc.gov.educ.api.pen.myed.util.LocalDateTimeUtil.getAPIFormattedDateOfBirth(request.getBirthDate()))")
  @Mapping(target = "demogCode", constant = "A")
  @Mapping(target = "deceasedDate", ignore = true)
  Student toStudent(Request request, String pen);
}
