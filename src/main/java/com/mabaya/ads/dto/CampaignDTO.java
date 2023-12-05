package com.mabaya.ads.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * DTO class for {@link com.mabaya.ads.model.Campaign}.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
public record CampaignDTO(
    Optional<Long> id,
    @NotBlank String name,
    @NotNull Instant startDate,
    @NotNull BigDecimal bid,
    @NotEmpty Collection<Long> productIds) {}
