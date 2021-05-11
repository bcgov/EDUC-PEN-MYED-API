package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.mappers.LocalDateTimeMapper;
import ca.bc.gov.educ.api.pen.myed.mappers.UUIDMapper;
import ca.bc.gov.educ.api.pen.myed.struct.v1.MyEdStudent;
import ca.bc.gov.educ.api.pen.myed.struct.v1.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
public interface MyEdStudentMapper {
  MyEdStudentMapper mapper = Mappers.getMapper(MyEdStudentMapper.class);

  @Mapping(target = "usualSurname", source = "usualLastName")
  @Mapping(target = "usualGivenName", source = "usualFirstName")
  @Mapping(target = "legalSurname", source = "legalLastName")
  @Mapping(target = "legalGivenName", source = "legalFirstName")
  @Mapping(target = "gender", source = "genderCode")
  @Mapping(target = "enrolledGradeCode", source = "gradeCode")
  @Mapping(target = "birthDate", expression = "java(org.apache.commons.lang3.RegExUtils.removeAll(student.getDob(), \"[^\\\\d]\" ) )")
  MyEdStudent toMyEdStudent(Student student);
}
