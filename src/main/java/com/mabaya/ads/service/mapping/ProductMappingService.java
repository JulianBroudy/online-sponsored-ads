package com.mabaya.ads.service.mapping;

import com.mabaya.ads.dto.ProductDTO;
import com.mabaya.ads.model.Product;
import org.springframework.stereotype.Service;

/**
 * Todo ExplainTheClass.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Service
public class ProductMappingService implements IMappingService<Product, ProductDTO> {
  @Override
  public ProductDTO mapToDTO(Product model) {
    return new ProductDTO(
        model.getId(),
        model.getTitle(),
        model.getPrice(),
        model.getCategory(),
        model.getSerialNumber());
  }

  @Override
  public Product mapToModel(ProductDTO dto) {
    return new Product(dto.id(), dto.title(), dto.price(), dto.category(), dto.serialNumber());
  }
}
