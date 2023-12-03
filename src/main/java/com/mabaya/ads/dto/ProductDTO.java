package com.mabaya.ads.dto;

import com.mabaya.ads.model.Category;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Todo ExplainTheClass.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
public record ProductDTO(
    Long id, String title, BigDecimal price, Category category, String serialNumber) {
  public ProductDTO {
    Objects.requireNonNull(title, "Title must not be null");
    Objects.requireNonNull(price, "Price must not be null");
    Objects.requireNonNull(category, "Category must not be null");
    Objects.requireNonNull(serialNumber, "Serial number must not be null");
  }
}
