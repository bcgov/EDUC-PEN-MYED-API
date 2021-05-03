package ca.bc.gov.educ.api.pen.myed.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The type Application properties.
 */
@Component
@Getter
@Setter
public class ApplicationProperties {

  /**
   * The constant API_NAME.
   */
  public static final String API_NAME = "PEN_MYED_API";
  /**
   * The Client id.
   */
  @Value("${client.id}")
  private String clientID;
  /**
   * The Client secret.
   */
  @Value("${client.secret}")
  private String clientSecret;
  /**
   * The Token url.
   */
  @Value("${url.token}")
  private String tokenURL;

  /**
   * The Pen reg batch api url.
   */
  @Value("${url.api.pen.reg.batch}")
  private String penRegBatchApiUrl;

  /**
   * The School api url.
   */
  @Value("${url.api.school}")
  private String schoolApiUrl;

  /**
   * The Pen services api url.
   */
  @Value("${url.api.pen.services}")
  private String penServicesApiURL;

  /**
   * The Pen match api url.
   */
  @Value("${url.api.pen.match}")
  private String penMatchApiURL;

  /**
   * The Student api url.
   */
  @Value("${url.api.student}")
  private String studentApiURL;

}
