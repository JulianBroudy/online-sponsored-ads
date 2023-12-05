package com.mabaya.ads.configuration;

import com.mabaya.ads.controller.V1RestController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class is responsible for prefixing api paths to endpoints.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Configuration
public class RestControllersConfiguration implements WebMvcConfigurer {
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix("api/v1", HandlerTypePredicate.forAnnotation(V1RestController.class));
  }
}
