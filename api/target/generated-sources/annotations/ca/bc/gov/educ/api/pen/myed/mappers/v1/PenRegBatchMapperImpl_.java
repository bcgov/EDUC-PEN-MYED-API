package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmission;
import ca.bc.gov.educ.api.pen.myed.struct.v1.PenRequestBatchSubmissionStudent;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatch.PenRequestBatchBuilder;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatchStudent;
import ca.bc.gov.educ.api.pen.myed.struct.v1.penregbatch.PenRequestBatchStudent.PenRequestBatchStudentBuilder;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-04-28T14:24:46-0700",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.7 (Oracle Corporation)"
)
public class PenRegBatchMapperImpl_ implements PenRegBatchMapper {

    @Override
    public PenRequestBatch toPenRequestBatch(PenRequestBatchSubmission penRequestBatchSubmission) {
        if ( penRequestBatchSubmission == null ) {
            return null;
        }

        PenRequestBatchBuilder penRequestBatch = PenRequestBatch.builder();

        penRequestBatch.sisVendorName( penRequestBatchSubmission.getVendorName() );
        penRequestBatch.sisProductName( penRequestBatchSubmission.getProductName() );
        penRequestBatch.sisProductID( penRequestBatchSubmission.getProductID() );
        penRequestBatch.submissionNumber( penRequestBatchSubmission.getSubmissionNumber() );
        penRequestBatch.mincode( penRequestBatchSubmission.getMincode() );
        penRequestBatch.schoolName( penRequestBatchSubmission.getSchoolName() );
        penRequestBatch.schoolGroupCode( penRequestBatchSubmission.getSchoolGroupCode() );
        penRequestBatch.createUser( penRequestBatchSubmission.getCreateUser() );
        penRequestBatch.updateUser( penRequestBatchSubmission.getUpdateUser() );

        penRequestBatch.sourceApplication( "MyED" );
        penRequestBatch.penRequestBatchTypeCode( "SCHOOL" );
        penRequestBatch.penRequestBatchStatusCode( "LOADED" );
        penRequestBatch.penRequestBatchProcessTypeCode( "API" );
        penRequestBatch.ministryPRBSourceCode( "TSWPENWEB" );
        penRequestBatch.fileType( "PEN" );

        return penRequestBatch.build();
    }

    @Override
    public PenRequestBatchStudent toPenRequestBatchStudent(PenRequestBatchSubmissionStudent penRequestBatchSubmissionStudent) {
        if ( penRequestBatchSubmissionStudent == null ) {
            return null;
        }

        PenRequestBatchStudentBuilder penRequestBatchStudent = PenRequestBatchStudent.builder();

        penRequestBatchStudent.usualMiddleNames( penRequestBatchSubmissionStudent.getUsualMiddleName() );
        penRequestBatchStudent.usualLastName( penRequestBatchSubmissionStudent.getUsualSurname() );
        penRequestBatchStudent.usualFirstName( penRequestBatchSubmissionStudent.getUsualGivenName() );
        penRequestBatchStudent.legalMiddleNames( penRequestBatchSubmissionStudent.getLegalMiddleName() );
        penRequestBatchStudent.legalLastName( penRequestBatchSubmissionStudent.getLegalSurname() );
        penRequestBatchStudent.legalFirstName( penRequestBatchSubmissionStudent.getLegalGivenName() );
        penRequestBatchStudent.submittedPen( penRequestBatchSubmissionStudent.getPen() );
        penRequestBatchStudent.gradeCode( penRequestBatchSubmissionStudent.getEnrolledGradeCode() );
        penRequestBatchStudent.genderCode( penRequestBatchSubmissionStudent.getGender() );
        penRequestBatchStudent.dob( penRequestBatchSubmissionStudent.getBirthDate() );
        penRequestBatchStudent.localID( penRequestBatchSubmissionStudent.getLocalStudentID() );
        penRequestBatchStudent.postalCode( penRequestBatchSubmissionStudent.getPostalCode() );

        penRequestBatchStudent.penRequestBatchStudentStatusCode( "LOADED" );

        return penRequestBatchStudent.build();
    }
}
