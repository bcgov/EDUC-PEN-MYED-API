package ca.bc.gov.educ.api.pen.myed.struct.v1.school;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PenCoordinator {
  String districtNumber;
  String schoolNumber;
  String mincode;
  String penCoordinatorName;
  String penCoordinatorEmail;
  String penCoordinatorFax;
  String sendPenResultsVia;
}
