package ca.bc.gov.educ.api.pen.myed.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class holds all application properties
 *
 * @author Marco Villeneuve
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

  @Value("${url.api.pen.reg.batch}")
  private String penRegBatchApiUrl;

  @Value("${url.api.school}")
  private String schoolApiUrl;

  @Value("${url.api.pen.services}")
  private String penServicesApiURL;

}
