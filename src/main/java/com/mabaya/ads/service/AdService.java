package com.mabaya.ads.service;

import com.mabaya.ads.dto.AdDTO;
import com.mabaya.ads.dto.CampaignDTO;
import com.mabaya.ads.dto.ProductDTO;
import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.model.Product;
import com.mabaya.ads.repository.CampaignRepository;
import com.mabaya.ads.repository.ProductRepository;
import com.mabaya.ads.service.mapping.IMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;

/**
 * Todo ExplainTheClass.
 * TODO Do we need @Transactional annotations here?
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Service
public class AdService {
  private final CampaignRepository campaignRepository;

  @Autowired
  public AdService(CampaignRepository campaignRepository) {
    this.campaignRepository = campaignRepository;
  }

  public ProductDTO getPromotedProductWithHighestBid(Category category) {
  }
}
