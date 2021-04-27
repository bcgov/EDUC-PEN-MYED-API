package ca.bc.gov.educ.api.pen.myed.struct.v1.penmatch;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * The type Pen match result.
 */
@Data
@AllArgsConstructor
public class PenMatchResult {

  /**
   * The Matching records.
   */
  private List<PenMatchRecord> matchingRecords;
  /**
   * The Pen status.
   */
  private String penStatus;
  /**
   * The Pen status message.
   */
  private String penStatusMessage;
}
