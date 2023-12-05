package com.mabaya.ads.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.web.bind.annotation.RestController;

/**
 * Custom annotation for marking REST controllers as version 1. This annotation is used to
 * categorize and apply common configurations to all controllers handling version 1 of the API.
 *
 * <p>It is a meta-annotation that combines {@link RestController} with versioning information. When
 * used, it indicates that the annotated class is a REST controller specifically for 'v1' API
 * endpoints.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
public @interface V1RestController {}
