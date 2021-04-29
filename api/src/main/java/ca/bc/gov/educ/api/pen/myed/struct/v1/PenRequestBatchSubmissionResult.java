package ca.bc.gov.educ.api.pen.myed.struct.v1;

import lombok.Data;

import java.util.List;

@Data
public class PenRequestBatchSubmissionResult {
  List<ListItem> pendingList;
  List<ListItem> newPenAssignedList;
  List<ListItem> exactMatchList;
  List<SchoolMinListItem> differencesList;
  List<SchoolMinListItem> confirmedList;
}
