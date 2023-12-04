package com.mabaya.ads.controller;

import com.mabaya.ads.dto.ProductDTO;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Todo ExplainTheClass.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@V1RestController
@RequestMapping(path = "/ad")
public class AdController {

  private final AdService adService;

  @Autowired
  public AdController(AdService adService) {
    this.adService = adService;
  }

  @PostMapping("/{category}")
  public ResponseEntity<ProductDTO> serveAd(@PathVariable Category category) {
    try {
      final ProductDTO promotedProduct = adService.getPromotedProductWithHighestBid(category);
      return new ResponseEntity<>(promotedProduct, HttpStatus.OK); // Changed to OK for GET request
    } catch (Exception e) {
      e.printStackTrace(); // TODO Move exceptions to LOGGERs.
      // TODO Handle different types of exceptions with more specific messages
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}
