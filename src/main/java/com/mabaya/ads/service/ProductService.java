package com.mabaya.ads.service;

import com.mabaya.ads.dto.ProductDTO;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.model.Product;
import com.mabaya.ads.repository.ProductRepository;
import com.mabaya.ads.service.mapping.IMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages the lifecycle and operations of products. This service provides functionalities for
 * validating, retrieving, and persisting product information, using {@link ProductDTO} and {@link
 * Category}.
 *
 * <p>It utilizes {@link ProductRepository} for database interactions and {@link IMapper} for
 * mapping between entity and DTO. The {@link Transactional} annotation is used to ensure proper
 * transaction management during database operations.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 * @see Product
 * @see ProductDTO
 * @see Category
 * @see IMapper
 */
@Service
public class ProductService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;
  private final IMapper<Product, ProductDTO> productMapper;

  @Autowired
  public ProductService(
      ProductRepository productRepository, IMapper<Product, ProductDTO> productMapper) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }

  /**
   * Fetches and validates a set of products based on the provided product IDs. Validates that each
   * ID corresponds to an existing product in the database.
   *
   * @param productIds A collection of product IDs to validate and fetch.
   * @return A set of {@link Product} objects corresponding to the provided IDs.
   * @throws IllegalArgumentException If the productIds collection is null or empty.
   * @throws NoSuchElementException If any of the product IDs do not correspond to an existing
   *     product.
   */
  @Transactional(readOnly = true)
  public Set<Product> fetchAndValidateProducts(Collection<Long> productIds) {
    LOGGER.info("Validating product IDs: {}", productIds);
    if (productIds == null || productIds.isEmpty()) {
      LOGGER.warn("Product IDs are null or empty");
      throw new IllegalArgumentException("Product IDs cannot be null or empty");
    }

    final Set<Product> products = new HashSet<>(productRepository.findAllById(productIds));

    if (products.size() != productIds.size()) {
      final Set<Long> missingIds = new HashSet<>(productIds);
      missingIds.removeAll(products.stream().map(Product::getId).collect(Collectors.toSet()));
      throw new NoSuchElementException("Products not found for IDs: " + missingIds);
    }
    LOGGER.debug("Products fetched and validated successfully");
    return products;
  }

  /**
   * Counts the total number of products available in the repository.
   *
   * @return The total count of products.
   */
  public long countAllProducts() {
    return productRepository.count();
  }

  /**
   * Persists a single product based on the provided {@link ProductDTO}.
   *
   * @param productDTO The DTO containing product details for persistence.
   * @return The persisted {@link Product} entity.
   */
  public Product persistProduct(ProductDTO productDTO) {
    // Required validation performed by jakarta's validation annotation at dto creation time
    return productRepository.save(productMapper.mapToModel(productDTO));
  }

  /**
   * Persists a list of products.
   *
   * @param products The list of {@link Product} entities to be persisted.
   * @return A list of persisted {@link Product} entities.
   */
  @Transactional
  public List<Product> persistProducts(List<Product> products) {
    return productRepository.saveAll(products);
  }

  /**
   * Retrieves the product with the highest price from a collection of products.
   *
   * @param products A collection of products to evaluate.
   * @return The {@link Product} with the highest price.
   * @throws NoSuchElementException if no product is found in the given collection.
   */
  public Product getProductWithHighestPrice(Collection<Product> products) {
    return getProductWithHighestPrice(products, null);
  }

  /**
   * Retrieves the product with the highest price within a specified category from a collection of
   * products.
   *
   * @param products A collection of products to evaluate.
   * @param category The category to filter the products by.
   * @return The {@link Product} with the highest price within the specified category.
   * @throws NoSuchElementException if no product matching the criteria is found.
   */
  public Product getProductWithHighestPrice(Collection<Product> products, Category category) {
    return products.stream()
        // Filter only if category is not null
        .filter(product -> category == null || product.getCategory().equals(category))
        // Find product with the maximum price
        .max(Comparator.comparing(Product::getPrice))
        // Throw an exception if no product is found
        .orElseThrow(
            () -> new NoSuchElementException("No product found for the specified criteria"));
  }

  /**
   * Converts a {@link Product} entity to its corresponding {@link ProductDTO}.
   *
   * @param product The Product entity to be converted.
   * @return The corresponding ProductDTO.
   */
  public ProductDTO getDTO(Product product) {
    return productMapper.mapToDTO(product);
  }
}
