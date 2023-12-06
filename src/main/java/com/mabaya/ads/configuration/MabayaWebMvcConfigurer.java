package com.mabaya.ads.configuration;

import com.mabaya.ads.controller.V1RestController;
import com.mabaya.ads.model.Category;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class that sets up path prefixing for REST controllers and case-insensitive {@link
 * Category}.
 *
 * <p>It ensures that all controllers annotated with {@link V1RestController} are prefixed with
 * 'api/v1', effectively namespacing the API version.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Configuration
public class MabayaWebMvcConfigurer implements WebMvcConfigurer {
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix("api/v1", HandlerTypePredicate.forAnnotation(V1RestController.class));
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(
        (Converter<String, Category>) source -> Category.valueOf(source.toUpperCase()));
  }
}
