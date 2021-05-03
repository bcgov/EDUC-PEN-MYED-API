package ca.bc.gov.educ.api.pen.myed.struct.v1.school;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * The type School.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class School implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The Dist no.
   */
  @Size(max = 3)
  @NotNull(message = "distNo can not be null.")
  private String distNo;

  /**
   * The Schl no.
   */
  @Size(max = 5)
  @NotNull(message = "schlNo can not be null.")
  private String schlNo;

  /**
   * The Sc address line 1.
   */
  @Size(max = 40)
  private String scAddressLine1;

  /**
   * The Sc address line 2.
   */
  @Size(max = 40)
  private String scAddressLine2;

  /**
   * The Sc city.
   */
  @Size(max = 30)
  private String scCity;

  /**
   * The Sc province code.
   */
  @Size(max = 2)
  private String scProvinceCode;

  /**
   * The Sc country code.
   */
  @Size(max = 3)
  private String scCountryCode;

  /**
   * The Sc postal code.
   */
  @Size(max = 6)
  private String scPostalCode;

  /**
   * The Sc fax number.
   */
  @Size(max = 10)
  private String scFaxNumber;

  /**
   * The Sc phone number.
   */
  @Size(max = 10)
  private String scPhoneNumber;

  /**
   * The Sc e mail id.
   */
  @Size(max = 100)
  private String scEMailId;

  /**
   * The Facility type code.
   */
  @Size(max = 2)
  private String facilityTypeCode;

  /**
   * The School name.
   */
  @Size(max = 40)
  private String schoolName;

  /**
   * The School type code.
   */
  @Size(max = 2)
  private String schoolTypeCode;

  /**
   * The School organization code.
   */
  @Size(max = 3)
  private String schoolOrganizationCode;

  /**
   * The School category code.
   */
  @Size(max = 2)
  private String schoolCategoryCode;

  /**
   * The Pr given name.
   */
  @Size(max = 25)
  private String prGivenName;

  /**
   * The Pr surname.
   */
  @Size(max = 25)
  private String prSurname;

  /**
   * The Pr middle name.
   */
  @Size(max = 25)
  private String prMiddleName;

  /**
   * The Pr title code.
   */
  @Size(max = 2)
  private String prTitleCode;

  /**
   * The Number of divisions.
   */
  private Long numberOfDivisions;

  /**
   * The Number of sec fte teachers.
   */
  private Long numberOfSecFteTeachers;

  /**
   * The Number of elm fte teachers.
   */
  private Long numberOfElmFteTeachers;

  /**
   * The Ttbl elem instr minutes.
   */
  private Long ttblElemInstrMinutes;

  /**
   * The School status code.
   */
  @Size(max = 1)
  private String schoolStatusCode;

  /**
   * The Enrol headcount 1523.
   */
  private Long enrolHeadcount1523;

  /**
   * The Enrol headcount 1701.
   */
  private Long enrolHeadcount1701;

  /**
   * The Grade 01 ind.
   */
  @Size(max = 1)
  private String grade01Ind;

  /**
   * The Grade 29 ind.
   */
  @Size(max = 1)
  private String grade29Ind;

  /**
   * The Grade 04 ind.
   */
  @Size(max = 1)
  private String grade04Ind;

  /**
   * The Grade 05 ind.
   */
  @Size(max = 1)
  private String grade05Ind;

  /**
   * The Grade 06 ind.
   */
  @Size(max = 1)
  private String grade06Ind;

  /**
   * The Grade 07 ind.
   */
  @Size(max = 1)
  private String grade07Ind;

  /**
   * The Grade 08 ind.
   */
  @Size(max = 1)
  private String grade08Ind;

  /**
   * The Grade 09 ind.
   */
  @Size(max = 1)
  private String grade09Ind;

  /**
   * The Grade 10 ind.
   */
  @Size(max = 1)
  private String grade10Ind;

  /**
   * The Grade 11 ind.
   */
  @Size(max = 1)
  private String grade11Ind;

  /**
   * The Grade 12 ind.
   */
  @Size(max = 1)
  private String grade12Ind;

  /**
   * The Grade 79 ind.
   */
  @Size(max = 1)
  private String grade79Ind;

  /**
   * The Grade 89 ind.
   */
  @Size(max = 1)
  private String grade89Ind;

  /**
   * The Opened date.
   */
  @Size(max = 8)
  private String openedDate;

  /**
   * The Closed date.
   */
  @Size(max = 8)
  private String closedDate;

  /**
   * The Auth number.
   */
  @Size(max = 3)
  private String authNumber;

  /**
   * The Create date.
   */
  private Long createDate;

  /**
   * The Create time.
   */
  private Long createTime;

  /**
   * The Create username.
   */
  @Size(max = 12)
  private String createUsername;

  /**
   * The Edit date.
   */
  private Long editDate;

  /**
   * The Edit time.
   */
  private Long editTime;

  /**
   * The Edit username.
   */
  @Size(max = 12)
  private String editUsername;

  /**
   * The Elem teachers hc.
   */
  private Long elemTeachersHc;

  /**
   * The Sec teachers hc.
   */
  private Long secTeachersHc;

  /**
   * The Grade kh ind.
   */
  @Size(max = 1)
  private String gradeKhInd;

  /**
   * The Grade kf ind.
   */
  @Size(max = 1)
  private String gradeKfInd;

  /**
   * The Grade 02 ind.
   */
  @Size(max = 1)
  private String grade02Ind;

  /**
   * The Grade 03 ind.
   */
  @Size(max = 1)
  private String grade03Ind;

  /**
   * The Grade eu ind.
   */
  @Size(max = 1)
  private String gradeEuInd;

  /**
   * The Grade su ind.
   */
  @Size(max = 1)
  private String gradeSuInd;

  /**
   * The Grade hs ind.
   */
  @Size(max = 1)
  private String gradeHsInd;

  /**
   * The Conted fund flag.
   */
  @Size(max = 4)
  private String contedFundFlag;

  /**
   * The Elem fte classroom.
   */
  private Long elemFteClassroom;

  /**
   * The Elem fte support.
   */
  private Long elemFteSupport;

  /**
   * The Elem fte admin.
   */
  private Long elemFteAdmin;

  /**
   * The Sec fte classroom.
   */
  private Long secFteClassroom;

  /**
   * The Sec fte support.
   */
  private Long secFteSupport;

  /**
   * The Sec fte admin.
   */
  private Long secFteAdmin;

  /**
   * The Phys address line 1.
   */
  @Size(max = 40)
  private String physAddressLine1;

  /**
   * The Phys address line 2.
   */
  @Size(max = 40)
  private String physAddressLine2;

  /**
   * The Phys city.
   */
  @Size(max = 30)
  private String physCity;

  /**
   * The Phys province code.
   */
  @Size(max = 2)
  private String physProvinceCode;

  /**
   * The Phys country code.
   */
  @Size(max = 3)
  private String physCountryCode;

  /**
   * The Phys postal code.
   */
  @Size(max = 6)
  private String physPostalCode;

  /**
   * The Educ method class cnt.
   */
  private Long educMethodClassCnt;

  /**
   * The Educ method del cnt.
   */
  private Long educMethodDelCnt;

  /**
   * The Educ method both cnt.
   */
  private Long educMethodBothCnt;

  /**
   * The New distno.
   */
  @Size(max = 3)
  private String newDistno;

  /**
   * The New schlno.
   */
  @Size(max = 5)
  private String newSchlno;

  /**
   * The Date opened.
   */
  private String dateOpened;

  /**
   * The Date closed.
   */
  private String dateClosed;

  /**
   * The Asset number.
   */
  private Long assetNumber;

  /**
   * The Asset assigned by.
   */
  @Size(max = 12)
  private String assetAssignedBy;

  /**
   * The Asset assigned date.
   */
  private String assetAssignedDate;

  /**
   * The Asset changed by.
   */
  @Size(max = 12)
  private String assetChangedBy;

  /**
   * The Asset changed date.
   */
  private String assetChangedDate;

  /**
   * The Restrict funding.
   */
  @Size(max = 1)
  private String restrictFunding;

  /**
   * The Grade ga ind.
   */
  @Size(max = 1)
  private String gradeGaInd;

  /**
   * The Nlc early learning flag.
   */
  @Size(max = 1)
  private String nlcEarlyLearningFlag;

  /**
   * The Nlc after school program flag.
   */
  @Size(max = 1)
  private String nlcAfterSchoolProgramFlag;

  /**
   * The Nlc continuing ed flag.
   */
  @Size(max = 1)
  private String nlcContinuingEdFlag;

  /**
   * The Nlc seniors flag.
   */
  @Size(max = 1)
  private String nlcSeniorsFlag;

  /**
   * The Nlc sport and rec flag.
   */
  @Size(max = 1)
  private String nlcSportAndRecFlag;

  /**
   * The Nlc community use flag.
   */
  @Size(max = 1)
  private String nlcCommunityUseFlag;

  /**
   * The Nlc integrated services flag.
   */
  @Size(max = 1)
  private String nlcIntegratedServicesFlag;

}
