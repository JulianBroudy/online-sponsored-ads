package com.mabaya.ads.service;

import com.mabaya.ads.dto.CampaignDTO;
import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Product;
import com.mabaya.ads.repository.CampaignRepository;
import com.mabaya.ads.repository.ProductRepository;
import com.mabaya.ads.service.mapping.IMappingService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    List<Product> list = new ArrayList<>();
    for (Long id : productIds) {
      Product product = productRepository.findById(id).orElseThrow();
      list.add(product);
    }
    return list;
  }

  private void validateCampaignData(CampaignDTO campaignDTO) {
    // TODO Validate the campaignDTO data (check if the product identifiers exist, etc.)     //
    // Throw relevant exceptions if validation fails
  }

  public List<CampaignDTO> getCampaigns() {
    Collection<Campaign> campaigns = campaignRepository.findAll();
    return campaignMappingService.mapToDTO(campaigns).stream().toList();
  }
}
