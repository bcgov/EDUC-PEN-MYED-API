package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;

import java.util.List;

/**
 * The type Pen request batch submission result.
 */
@Data
public class MyEdSubmissionResult {
  /**
   * The Pending list.
   */
  List<MyEdListItem> pendingList;
  /**
   * The New pen assigned list.
   */
  List<MyEdListItem> newPenAssignedList;
  /**
   * The Exact match list.
   */
  List<MyEdListItem> exactMatchList;
  /**
   * The Differences list.
   */
  List<MyEdSchoolMinListItem> differencesList;
  /**
   * The Confirmed list.
   */
  List<MyEdSchoolMinListItem> confirmedList;
}
