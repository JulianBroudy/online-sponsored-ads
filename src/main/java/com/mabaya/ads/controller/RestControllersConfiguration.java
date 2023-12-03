package com.mabaya.ads.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Todo ExplainTheClass.
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
