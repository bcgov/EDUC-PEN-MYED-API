package ca.bc.gov.educ.api.pen.myed.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Pen my ed apimvc config.
 */
@Configuration
public class PenMyEdAPIMVCConfig implements WebMvcConfigurer {

  /**
   * The Interceptor.
   */
  @Getter(AccessLevel.PRIVATE)
  private final PenMyEdAPIInterceptor interceptor;

  /**
   * Instantiates a new Pen my ed apimvc config.
   *
   * @param interceptor the interceptor
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
