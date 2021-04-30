package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.mappers.LocalDateTimeMapper;
import ca.bc.gov.educ.api.pen.myed.mappers.UUIDMapper;
import ca.bc.gov.educ.api.pen.myed.struct.v1.Request;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penmatch.PenMatchStudent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
public interface PenMatchMapper {
  PenMatchMapper mapper = Mappers.getMapper(PenMatchMapper.class);

  @Mapping(target = "surname", source = "legalSurname")
  @Mapping(target = "sex", source = "gender")
  @Mapping(target = "postal", source = "postalCode")
  @Mapping(target = "middleName", source = "legalMiddleName")
  @Mapping(target = "givenName", source = "legalGivenName")
  @Mapping(target = "localID", source = "localStudentID")
  @Mapping(target = "dob", source = "birthDate")
  PenMatchStudent toPenMatchStudent(Request request);
}
