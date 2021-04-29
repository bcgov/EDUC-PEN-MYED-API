package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionStudent;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatchStudent;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-04-28T14:24:47-0700",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.7 (Oracle Corporation)"
)
public class PenRegBatchMapperImpl extends PenRegBatchDecorator implements PenRegBatchMapper {

    private final PenRegBatchMapper delegate;

    public PenRegBatchMapperImpl() {
        this( new PenRegBatchMapperImpl_() );
    }

    private PenRegBatchMapperImpl(PenRegBatchMapperImpl_ delegate) {
        super( delegate );
        this.delegate = delegate;
    }

    @Override
    public PenRequestBatchStudent toPenRequestBatchStudent(PenRequestBatchSubmissionStudent penRequestBatchSubmissionStudent)  {
        return delegate.toPenRequestBatchStudent( penRequestBatchSubmissionStudent );
    }
}
