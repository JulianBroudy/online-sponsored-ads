package com.mabaya.ads.service.mapping;

import com.mabaya.ads.dto.CampaignDTO;
import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Product;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles mapping between {@link Campaign} entity and {@link CampaignDTO}. Facilitates conversions
 * in both directions while ensuring data integrity and structure.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 * @see Campaign
 * @see CampaignDTO
 */
@Component
public class CampaignMapper implements IMapper<Campaign, CampaignDTO> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CampaignMapper.class);

  /**
   * Maps {@link Campaign} entity to {@link CampaignDTO}. Transfers entity properties to a DTO
   * format, including associated product IDs.
   *
   * @param model the Campaign entity to be mapped.
   * @return Corresponding CampaignDTO.
   */
  @Override
  public CampaignDTO mapToDTO(Campaign model) {
    LOGGER.debug("Mapping Campaign model to DTO");
    return new CampaignDTO(
        Optional.of(model.getId()),
        model.getName(),
        model.getStartDate(),
        model.getBid(),
        model.getProducts().stream().map(Product::getId).collect(Collectors.toSet()));
  }

  /**
   * Maps {@link CampaignDTO} to {@link Campaign} entity. Handles creation and updates by mapping
   * DTO fields to the Campaign entity.
   *
   * @param dto the CampaignDTO to be mapped.
   * @return Corresponding Campaign entity.
   */
  @Override
  public Campaign mapToModel(CampaignDTO dto) {
    LOGGER.debug("Mapping DTO to Campaign model");
    return dto.id()
        .map(id -> new Campaign(id, dto.name(), dto.startDate(), dto.bid(), null))
        .orElseGet(() -> new Campaign(dto.name(), dto.startDate(), dto.bid()));
  }
}
