package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
public class PenRequestBatchSubmission {
  @Size(min = 8, max = 8)
  @NotNull
  String mincode;
  @Size(min = 8, max = 8)
  @NotBlank
  String submissionNumber;
  @Size(max = 100)
  @NotBlank
  String vendorName;
  @Size(max = 100)
  @NotBlank
  String productName;
  @Size(max = 15)
  @NotBlank
  String productID;
  @Size(max = 6)
  @NotNull
  String studentCount;
  @NotBlank
  String schoolGroupCode;
  @NotBlank
  String schoolName;
  @NotBlank
  String createUser;
  @NotBlank
  String updateUser;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @NotEmpty
  @Valid
  List<PenRequestBatchSubmissionStudent> students;
}


