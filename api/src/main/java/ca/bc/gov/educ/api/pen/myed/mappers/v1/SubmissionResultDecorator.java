package ca.bc.gov.educ.api.pen.myed.mappers.v1;

import ca.bc.gov.educ.api.pen.myed.struct.v1.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * The type Pen reg batch decorator.
 */
@Slf4j
public abstract class SubmissionResultDecorator implements SubmissionResultMapper {

  /**
   * The Mapper.
   */
  private final SubmissionResultMapper mapper;

  /**
   * Instantiates a new Pen reg batch decorator.
   *
   * @param mapper the mapper
   */
  protected SubmissionResultDecorator(final SubmissionResultMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public MyEdSubmissionResult toMyEdSubmissionResult(PenRequestBatchSubmissionResult result) {
    var listItemMapper = ListItemMapper.mapper;
    MyEdSubmissionResult myEdSubmissionResult = new MyEdSubmissionResult();
    myEdSubmissionResult.setExactMatchList(new ArrayList<>());
    myEdSubmissionResult.setDifferencesList(new ArrayList<>());
    myEdSubmissionResult.setNewPenAssignedList(new ArrayList<>());
    myEdSubmissionResult.setConfirmedList(new ArrayList<>());
    myEdSubmissionResult.setPendingList(new ArrayList<>());

    for(ListItem item: result.getExactMatchList()){
      myEdSubmissionResult.getExactMatchList().add(listItemMapper.toMyEdListItem(item));
    }

    for(ListItem item: result.getNewPenAssignedList()){
      myEdSubmissionResult.getNewPenAssignedList().add(listItemMapper.toMyEdListItem(item));
    }

    for(ListItem item: result.getPendingList()){
      myEdSubmissionResult.getPendingList().add(listItemMapper.toMyEdListItem(item));
    }

    for(SchoolMinListItem item: result.getConfirmedList()){
      MyEdSchoolMinListItem myEdSchoolMinListItem = new MyEdSchoolMinListItem();
      myEdSchoolMinListItem.setSchool(listItemMapper.toMyEdListItem(item.getSchool()));
      myEdSchoolMinListItem.setMin(listItemMapper.toMyEdListItem(item.getMin()));
      myEdSubmissionResult.getConfirmedList().add(myEdSchoolMinListItem);
    }

    for(SchoolMinListItem item: result.getDifferencesList()){
      MyEdSchoolMinListItem myEdSchoolMinListItem = new MyEdSchoolMinListItem();
      myEdSchoolMinListItem.setSchool(listItemMapper.toMyEdListItem(item.getSchool()));
      myEdSchoolMinListItem.setMin(listItemMapper.toMyEdListItem(item.getMin()));
      myEdSubmissionResult.getDifferencesList().add(myEdSchoolMinListItem);
    }

    return myEdSubmissionResult;
  }

}
