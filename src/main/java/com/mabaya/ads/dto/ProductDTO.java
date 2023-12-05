package com.mabaya.ads.dto;

import com.mabaya.ads.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * DTO class for {@link com.mabaya.ads.model.Product}.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
public record ProductDTO(
    Optional<Long> id,
    @NotBlank String title,
    @NotNull BigDecimal price,
    @NotNull Category category,
    @NotBlank String serialNumber) {}
