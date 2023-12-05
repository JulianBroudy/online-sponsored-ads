package com.mabaya.ads.service.mapping;

import com.mabaya.ads.dto.ProductDTO;
import com.mabaya.ads.model.Product;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Todo ExplainTheClass.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Component
public class ProductMapper implements IMapper<Product, ProductDTO> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductMapper.class);

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

  @Override
  public Product mapToModel(ProductDTO dto) {
    LOGGER.debug("Mapping DTO to Product model");
    if (dto.id().isPresent()) {
      return new Product(
          dto.id().get(), dto.title(), dto.price(), dto.category(), dto.serialNumber());
    } else {
      return new Product(dto.title(), dto.price(), dto.category(), dto.serialNumber());
    }
  }
}
