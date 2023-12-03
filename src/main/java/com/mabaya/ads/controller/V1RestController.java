package com.mabaya.ads.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.web.bind.annotation.RestController;

/**
 * Base RestController class for all 'v1' endpoints.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
public @interface V1RestController {}
