package ca.bc.gov.educ.api.pen.myed.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApplicationProperties {

  public static final String API_NAME = "PEN_MYED_API";
  @Value("${client.id}")
  private String clientID;
  @Value("${client.secret}")
  private String clientSecret;
  @Value("${url.token}")
  private String tokenURL;
  @Value("${url.api.pen.reg.batch}")
  private String penRegBatchApiUrl;
  @Value("${url.api.institute}")
  private String instituteApiUrl;
  @Value("${url.api.pen.services}")
  private String penServicesApiURL;
  @Value("${url.api.pen.match}")
  private String penMatchApiURL;
  @Value("${url.api.student}")
  private String studentApiURL;

}
