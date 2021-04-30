package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.mappers.LocalDateTimeMapper;
import ca.bc.gov.educ.api.pen.myed.mappers.UUIDMapper;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penservices.PenRequestStudentValidationPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
public interface PenServicesMapper {
  PenServicesMapper mapper = Mappers.getMapper(PenServicesMapper.class);
  @Mapping(target = "transactionID",  expression="java(java.util.UUID.randomUUID().toString())")
  @Mapping(target = "localID", source = "localStudentID")
  @Mapping(target = "isInteractive", expression="java(false)")
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
  PenRequestStudentValidationPayload toValidationPayload(Request request);

}
