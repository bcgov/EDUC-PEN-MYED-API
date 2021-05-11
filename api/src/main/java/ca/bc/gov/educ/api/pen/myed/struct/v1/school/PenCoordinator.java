package ca.bc.gov.educ.api.pen.myed.struct.v1.school;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Pen coordinator.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PenCoordinator {
  /**
   * The District number.
   */
  String districtNumber;
  /**
   * The School number.
   */
  String schoolNumber;
  /**
   * The Mincode.
   */
  String mincode;
  /**
   * The Pen coordinator name.
   */
  String penCoordinatorName;
  /**
   * The Pen coordinator email.
   */
  String penCoordinatorEmail;
  /**
   * The Pen coordinator fax.
   */
  String penCoordinatorFax;
}
