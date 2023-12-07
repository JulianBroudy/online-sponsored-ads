package com.mabaya.ads.controller;

import com.mabaya.ads.dto.ProductDTO;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.service.AdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for ad-related operations. This controller handles HTTP requests related to
 * serving ads, leveraging {@link AdService} to process ad serving logic.
 *
 * <p>It provides endpoints for serving ads based on product categories, ensuring proper error
 * handling and logging.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 * @see AdService
 * @see ProductDTO
 * @see Category
 */
@Tag(name = "Ad Controller", description = "The Ad API provides operations to serve ads.")
@V1RestController
@RequestMapping(path = "/ad")
public class AdController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AdController.class);

  private final AdService adService;

  @Autowired
  public AdController(AdService adService) {
    this.adService = adService;
  }

  /**
   * Serves an ad based on the specified product category. Retrieves the promoted product with the
   * highest bid within the active campaigns for the given category.
   *
   * @param category The category of products for which to serve an ad.
   * @return ResponseEntity containing the promoted ProductDTO or an appropriate error response.
   */
  @Operation(
      summary = "Serve an ad",
      description = "Serve a promoted product ad based on the specified category")
  @ApiResponse(responseCode = "200", description = "Successfully served an ad")
  @GetMapping("/{category}")
  public ResponseEntity<ProductDTO> serveAd(
      @Parameter(description = "Category of the product") @PathVariable Category category) {
    try {
      final ProductDTO promotedProduct = adService.getPromotedProductWithHighestBid(category);
      LOGGER.debug("Serving ad for category: {}", category);
      return ResponseEntity.ok(promotedProduct);
    } catch (NoSuchElementException e) {
      LOGGER.error("Error serving ad for category {}: {}", category, e.getMessage(), e);
      throw e; // Handled by GlobalExceptionHandler
    } catch (Exception e) {
      LOGGER.error("Unexpected error while serving ad: {}", e.getMessage(), e);
      throw e; // Handled by GlobalExceptionHandler
    }
  }
}
