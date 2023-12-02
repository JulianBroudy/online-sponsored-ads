package com.mabaya.ads.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Base RestController class for all 'v1' endpoints.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@RestController
@RequestMapping(path="api/v1/")
public abstract class V1BaseRestController {}
