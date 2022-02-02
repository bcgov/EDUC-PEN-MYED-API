package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.mappers.LocalDateTimeMapper;
import ca.bc.gov.educ.api.pen.myed.struct.v1.ListItem;
import ca.bc.gov.educ.api.pen.myed.struct.v1.MyEdListItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LocalDateTimeMapper.class})
public interface ListItemMapper {
  ListItemMapper mapper = Mappers.getMapper(ListItemMapper.class);

  @Mapping(target = "usualMiddleName", source = "usualMiddleNames")
  @Mapping(target = "legalMiddleName", source = "legalMiddleNames")
  MyEdListItem toMyEdListItem(ListItem student);
}
