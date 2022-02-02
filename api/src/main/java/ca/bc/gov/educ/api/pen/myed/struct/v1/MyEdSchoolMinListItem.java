package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;

/**
 * The type School min list item.
 */
@Data
public class MyEdSchoolMinListItem {
  /**
   * The School.
   */
  MyEdListItem school;
  /**
   * The Min.
   */
  MyEdListItem min;
}
