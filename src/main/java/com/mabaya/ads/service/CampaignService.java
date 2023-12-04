package com.mabaya.ads.service;

import com.mabaya.ads.dto.CampaignDTO;
import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.model.Product;
import com.mabaya.ads.repository.CampaignRepository;
import com.mabaya.ads.repository.ProductRepository;
import com.mabaya.ads.service.mapping.IMappingService;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Todo ExplainTheClass.
 *
 * <p>TODO Check @Transactional annotation usages here?
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Service
public class CampaignService {

  private final CampaignRepository campaignRepository;
  private final IMappingService<Campaign, CampaignDTO> campaignMappingService;
  private final ProductRepository productRepository;

  @Autowired
  public CampaignService(
      CampaignRepository campaignRepository,
      IMappingService<Campaign, CampaignDTO> campaignMappingService,
      ProductRepository productRepository) {
    this.campaignRepository = campaignRepository;
    this.campaignMappingService = campaignMappingService;
    this.productRepository = productRepository;
  }

  public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
    validateCampaignData(campaignDTO);
    Campaign campaign = campaignMappingService.mapToModel(campaignDTO);
    campaign.setProducts(productIdsToProducts(campaignDTO.productIds()));
    campaign = campaignRepository.save(campaign);
    return campaignMappingService.mapToDTO(campaign);
  }

  private Collection<Product> productIdsToProducts(Collection<Long> productIds) {
    final List<Product> list = new ArrayList<>();
    for (Long id : productIds) {
      list.add(productRepository.findById(id).orElseThrow());
    }
    return list;
  }

  private void validateCampaignData(CampaignDTO campaignDTO) {
    // TODO Validate the campaignDTO data (check if the product identifiers exist, etc.)     //
    // Throw relevant exceptions if validation fails
    // Validate the start date and ensure product identifiers exist
  }

  public List<CampaignDTO> getCampaigns() {
    Collection<Campaign> campaigns = campaignRepository.findAll();
    return campaignMappingService.mapToDTO(campaigns).stream().toList();
  }

  public Campaign getActiveCampaignWithHighestBid(Category category) {
    final long currentTimeMillis = System.currentTimeMillis();
    final Timestamp now = new Timestamp(currentTimeMillis);
    final Timestamp tenDaysAgo = new Timestamp(currentTimeMillis - TimeUnit.DAYS.toMillis(10));

    final PageRequest pageRequest = PageRequest.of(0, 1);

    List<Campaign> activeCampaigns =
        campaignRepository
            .findAllActiveCampaignWithHighestBidByCategory(now, tenDaysAgo, category, pageRequest)
            .getContent();
    if (!activeCampaigns.isEmpty()) {
      return activeCampaigns.get(0);
    } else {
      // Fallback
      activeCampaigns =
          campaignRepository
              .findAllActiveCampaignWithHighestBidByCategory(now, tenDaysAgo, null, pageRequest)
              .getContent();
      if (!activeCampaigns.isEmpty()) {
        return activeCampaigns.get(0);
      }
      // TODO make the exception specific
      throw new RuntimeException("No active campaigns found");
    }
  }
}
