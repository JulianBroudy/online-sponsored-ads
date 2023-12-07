package com.mabaya.ads.dto;

import com.mabaya.ads.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * DTO class for {@link com.mabaya.ads.model.Product}.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Schema(
    description =
        "Data Transfer Object representing a product. It includes the product's title, price, category, and serial number.",
    requiredProperties = {"title", "price", "category", "serialNumber"})
public record ProductDTO(
    @Schema(
            description = "Unique identifier of the product.",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
        Optional<Long> id,
    @NotBlank String title,
    @Schema(example = "197.12") @Positive BigDecimal price,
    @NotNull Category category,
    @NotBlank String serialNumber) {}
