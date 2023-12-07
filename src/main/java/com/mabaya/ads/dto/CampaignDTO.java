package com.mabaya.ads.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * DTO class for {@link com.mabaya.ads.model.Campaign}.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Schema(
    description =
        "Data Transfer Object representing a campaign. It includes details such as the campaign name, start date, bid amount, and associated product IDs.",
    requiredProperties = {"name", "startDate", "bid", "productIds"})
public record CampaignDTO(
    @Schema(
            description = "Unique identifier of the product.",
            example = "216",
            minimum = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
        Optional<Long> id,
    @NotBlank @Schema(example = "Creative Campaign Name") String name,
    @NotNull @FutureOrPresent @Schema(implementation = Instant.class) Instant startDate,
    @Positive @NotNull @Schema(example = "99.89", minimum = "0", exclusiveMinimum = true)
        BigDecimal bid,
    @ArraySchema(
            schema = @Schema(type = "long", minimum = "1", example = "[1, 3142, 9847, ...]"),
            minItems = 1)
        Collection<Long> productIds) {}
