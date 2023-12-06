package com.mabaya.ads.service;

import com.mabaya.ads.dto.ProductDTO;
import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.model.Product;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// While it's possible to eliminate this service and use the CampaignService or ProductService
// instead, chose to keep this to better align with the business logic and accounting for future
// expansion of features where AdService has more responsibilities.
/**
 * Manages ad-serving operations, particularly focusing on serving ads with the highest bid for a
 * given category. Utilizes {@link CampaignService} and {@link ProductService} to determine the most
 * appropriate product advertisement to serve.
 *
 * <p>This service is responsible for selecting the active campaign with the highest bid for a
 * specified category and subsequently finding the product with the highest price within that
 * campaign to serve as an ad.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 * @see CampaignService
 * @see ProductService
 * @see ProductDTO
 * @see Category
 */
@Service
public class AdService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AdService.class);

  private final CampaignService campaignService;
  private final ProductService productService;

  @Autowired
  public AdService(CampaignService campaignService, ProductService productService) {
    this.campaignService = campaignService;
    this.productService = productService;
  }

  /**
   * Retrieves the promoted product with the highest bid within an active campaign for the specified
   * category. The method first finds the active campaign with the highest bid, then selects the
   * product with the highest price from that campaign.
   *
   * @param category The category of products for which to serve an ad.
   * @return The {@link ProductDTO} of the product with the highest bid to be served as an ad.
   * @throws NoSuchElementException if no active campaign or suitable product is found.
   */
  public ProductDTO getPromotedProductWithHighestBid(Category category) {
    LOGGER.debug("Getting promoted product with highest bid for category {}", category);
    final Campaign activeCampaign = campaignService.getActiveCampaignWithHighestBid(category);
    final Product productWithHighestPrice =
        productService.getProductWithHighestPrice(activeCampaign.getProducts(), category);
    return productService.getDTO(productWithHighestPrice);
  }
}
