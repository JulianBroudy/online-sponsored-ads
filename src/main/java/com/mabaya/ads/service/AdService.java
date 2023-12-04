package com.mabaya.ads.service;

import com.mabaya.ads.dto.ProductDTO;
import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.model.Product;
import com.mabaya.ads.service.mapping.IMappingService;
import java.util.Comparator;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Todo ExplainTheClass. TODO Do we need @Transactional annotations here?
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Service
public class AdService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AdService.class);

  private final CampaignService campaignService;
  private final IMappingService<Product, ProductDTO> productMappingService;

  @Autowired
  public AdService(
      CampaignService campaignService, IMappingService<Product, ProductDTO> productMappingService) {
    this.campaignService = campaignService;
    this.productMappingService = productMappingService;
  }

  public ProductDTO getPromotedProductWithHighestBid(Category category) {
    final Campaign activeCampaign = campaignService.getActiveCampaignWithHighestBid(category);

    final Category finalCategory = category;
    Product mostExpensiveProduct =
        activeCampaign.getProducts().stream()
            // Filter only if category is not null
            .filter(product -> finalCategory == null || product.getCategory().equals(finalCategory))
            // Find product with the maximum price
            .max(Comparator.comparing(Product::getPrice))
            // Throw an exception if no product is found
            .orElseThrow(
                () -> new NoSuchElementException("No product found for the specified criteria"));

    // Now 'product' holds the required product or an exception is thrown
    return productMappingService.mapToDTO(mostExpensiveProduct);
  }
}
