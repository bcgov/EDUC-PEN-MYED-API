package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.mappers.LocalDateTimeMapper;
import ca.bc.gov.educ.api.pen.myed.mappers.UUIDMapper;
import ca.bc.gov.educ.api.pen.myed.struct.v1.MyEdSubmissionResult;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionResult;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
@DecoratedWith(SubmissionResultDecorator.class)
public interface SubmissionResultMapper {
  SubmissionResultMapper mapper = Mappers.getMapper(SubmissionResultMapper.class);

  MyEdSubmissionResult toMyEdSubmissionResult(PenRequestBatchSubmissionResult result);
}
