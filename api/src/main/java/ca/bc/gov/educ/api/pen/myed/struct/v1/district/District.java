package ca.bc.gov.educ.api.pen.myed.struct.v1.district;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * The type Student.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class District implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private String districtId;

  @Size(max = 3)
  @NotNull(message = "districtNumber can not be null.")
  private String districtNumber;

  @Size(max = 10)
  @Pattern(regexp = "^$|\\d{10}", message = "Invalid phone number format")
  private String faxNumber;

  @Size(max = 10)
  @Pattern(regexp = "^$|\\d{10}", message = "Invalid phone number format")
  private String phoneNumber;

  @Size(max = 255)
  @Email(message = "Email address should be a valid email address")
  private String email;

  @Size(max = 255)
  private String website;

  @Size(max = 255)
  @NotNull(message = "displayName cannot be null")
  private String displayName;

  @Size(max = 10)
  @NotNull(message = "districtRegionCode cannot be null")
  private String districtRegionCode;

  @Size(max = 10)
  @NotNull(message = "districtStatusCode cannot be null")
  private String districtStatusCode;

  @Size(max = 32)
  public String createUser;

  @Size(max = 32)
  public String updateUser;

  @Null(message = "createDate should be null.")
  public String createDate;

  @Null(message = "updateDate should be null.")
  public String updateDate;
}
