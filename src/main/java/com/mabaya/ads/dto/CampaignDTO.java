package com.mabaya.ads.dto;

import com.mabaya.ads.model.Product;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * DTO class for {@link com.mabaya.ads.model.Campaign}.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
public record CampaignDTO(
    Long id, String name, Timestamp startDate, BigDecimal bid, Collection<Product> products) {
  public CampaignDTO {
    Objects.requireNonNull(name, "Name must not be null");
    Objects.requireNonNull(startDate, "Start date must not be null");
    Objects.requireNonNull(bid, "Bid must not be null");
    Objects.requireNonNull(products, "Products must not be null");
  }
}
