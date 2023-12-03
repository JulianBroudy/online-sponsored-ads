package com.mabaya.ads.service.mapping;

import com.mabaya.ads.dto.CampaignDTO;
import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Product;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * This class defines the mapping operations between {@link Campaign} and {@link CampaignDTO}. //
 * TODO Should be annotated with `@Component` instead?
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Service
public class CampaignMappingService implements IMappingService<Campaign, CampaignDTO> {

  /**
   * Converts a {@link Campaign} entity to a {@link CampaignDTO}. This involves mapping the
   * campaign's properties to the corresponding DTO fields.
   *
   * @param model the Campaign entity to be mapped
   * @return the corresponding CampaignDTO
   */
  @Override
  public CampaignDTO mapToDTO(Campaign model) {
    return new CampaignDTO(
        model.getId(),
        model.getName(),
        model.getStartDate(),
        model.getBid(),
        model.getProducts().stream().map(Product::getId).collect(Collectors.toList()));
  }

  /**
   * Converts a {@link CampaignDTO} to a {@link Campaign} entity. This involves mapping the DTO's
   * properties to the corresponding campaign fields. Note: This method maps only the basic fields
   * and does not include the collection of products nor the id.
   *
   * @param dto the CampaignDTO to be mapped
   * @return the corresponding Campaign entity
   */
  @Override
  public Campaign mapToModel(CampaignDTO dto) {
    return new Campaign(dto.name(), dto.startDate(), dto.bid());
  }
}
