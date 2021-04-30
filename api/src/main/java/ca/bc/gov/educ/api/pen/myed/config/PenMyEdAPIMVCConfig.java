package ca.bc.gov.educ.api.pen.myed.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Pen reg api mvc config.
 *
 * @author Om
 */
@Configuration
public class PenMyEdAPIMVCConfig implements WebMvcConfigurer {

  /**
   * The Pen reg api interceptor.
   */
  @Getter(AccessLevel.PRIVATE)
  private final PenMyEdAPIInterceptor interceptor;

  /**
   * Instantiates a new Pen reg api mvc config.
   *
   * @param interceptor the pen reg api interceptor
   */
  @Autowired
  public PenMyEdAPIMVCConfig(final PenMyEdAPIInterceptor interceptor) {
    this.interceptor = interceptor;
  }

  /**
   * Add interceptors.
   *
   * @param registry the registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(interceptor).addPathPatterns("/**");
  }
}
