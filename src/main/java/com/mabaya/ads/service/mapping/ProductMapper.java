package com.mabaya.ads.service.mapping;

import com.mabaya.ads.dto.ProductDTO;
import com.mabaya.ads.model.Product;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles mapping between {@link Product} entity and {@link ProductDTO}. Facilitates conversions in
 * both directions while ensuring data integrity and structure.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 * @see Product
 * @see ProductDTO
 */
@Component
public class ProductMapper implements IMapper<Product, ProductDTO> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductMapper.class);

  /**
   * Maps {@link Product} entity to {@link ProductDTO}. Transfers entity properties to a DTO format,
   * including key attributes like ID, title, price, category, and serial number.
   *
   * @param model the Product entity to be mapped.
   * @return Corresponding ProductDTO.
   */
  @Override
  public ProductDTO mapToDTO(Product model) {
    LOGGER.debug("Mapping Product model to DTO");
    return new ProductDTO(
        Optional.of(model.getId()),
        model.getTitle(),
        model.getPrice(),
        model.getCategory(),
        model.getSerialNumber());
  }

  /**
   * Maps {@link ProductDTO} to {@link Product} entity. Handles creation and updates by mapping DTO
   * fields to the Product entity.
   *
   * @param dto the ProductDTO to be mapped.
   * @return Corresponding Product entity.
   */
  @Override
  public Product mapToModel(ProductDTO dto) {
    LOGGER.debug("Mapping DTO to Product model");
    return dto.id()
        .map(id -> new Product(id, dto.title(), dto.price(), dto.category(), dto.serialNumber()))
        .orElseGet(() -> new Product(dto.title(), dto.price(), dto.category(), dto.serialNumber()));
  }
}
