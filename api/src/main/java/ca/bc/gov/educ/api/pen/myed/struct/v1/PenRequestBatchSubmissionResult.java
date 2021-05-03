package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;

import java.util.List;

/**
 * The type Pen request batch submission result.
 */
@Data
public class PenRequestBatchSubmissionResult {
  /**
   * The Pending list.
   */
  List<ListItem> pendingList;
  /**
   * The New pen assigned list.
   */
  List<ListItem> newPenAssignedList;
  /**
   * The Exact match list.
   */
  List<ListItem> exactMatchList;
  /**
   * The Differences list.
   */
  List<SchoolMinListItem> differencesList;
  /**
   * The Confirmed list.
   */
  List<SchoolMinListItem> confirmedList;
}
