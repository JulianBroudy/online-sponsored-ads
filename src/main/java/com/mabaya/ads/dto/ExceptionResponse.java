package com.mabaya.ads.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Custom exception DTO.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Schema(
    name = "ExceptionResponse",
    description = "Custom response object for exceptions",
    type = "object")
public record ExceptionResponse(
    @Schema(
            description = "Error code representing the type of error",
            example = "Bad Request | Not Found | ...")
        String errorCode,
    @Schema(
            description = "A message describing the error",
            example = "Campaign start date cannot be in the past")
        String errorMessage,
    @Schema(
            description = "Timestamp when the error occurred",
            example = "2023-12-06T13:09:32.318738300Z")
        Instant timestamp) {}
