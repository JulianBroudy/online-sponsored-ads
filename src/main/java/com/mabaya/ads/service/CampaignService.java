package com.mabaya.ads.service;

import com.mabaya.ads.dto.CampaignDTO;
import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.model.Product;
import com.mabaya.ads.repository.CampaignRepository;
import com.mabaya.ads.service.mapping.IMapper;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages the lifecycle and operations of campaigns. This service provides functionalities for
 * creating, retrieving, and managing campaigns, leveraging {@link CampaignDTO} and {@link
 * Category}.
 *
 * <p>It uses {@link CampaignRepository} for data persistence, {@link IMapper} for mapping between
 * entity and DTO, and {@link ProductService} for additional validations. The {@link Transactional}
 * annotation ensures proper transaction management for database operations.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 * @see Campaign
 * @see CampaignDTO
 * @see Category
 * @see ProductService
 * @see IMapper
 */
@Service
public class CampaignService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CampaignService.class);

  private final CampaignRepository campaignRepository;
  private final IMapper<Campaign, CampaignDTO> campaignMapper;
  private final ProductService productService;

  @Autowired
  public CampaignService(
      CampaignRepository campaignRepository,
      IMapper<Campaign, CampaignDTO> campaignMapper,
      ProductService productService) {
    this.campaignRepository = campaignRepository;
    this.campaignMapper = campaignMapper;
    this.productService = productService;
  }

  /**
   * Creates and persists a new campaign based on the provided {@link CampaignDTO}. Validates the
   * campaign data before saving and links products to the campaign.
   *
   * @param campaignDTO The DTO containing campaign details.
   * @return A {@link CampaignDTO} representing the newly created and persisted campaign.
   * @throws IllegalArgumentException If the campaign data is invalid.
   */
  @Transactional
  public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
    LOGGER.debug("Creating a new campaign: {}", campaignDTO);
    validateCampaignData(campaignDTO);
    Set<Product> products = productService.fetchAndValidateProducts(campaignDTO.productIds());
    Campaign campaign = campaignMapper.mapToModel(campaignDTO);
    campaign.setProducts(products);
    LOGGER.info("Saving new campaign: {}", campaign);
    return campaignMapper.mapToDTO(campaignRepository.save(campaign));
  }

  private void validateCampaignData(CampaignDTO campaignDTO) {
    LOGGER.debug("Validating campaign data: {}", campaignDTO);
    if (campaignDTO.startDate().isBefore(Instant.now())) {
      throw new IllegalArgumentException("Campaign start date cannot be in the past");
    }
    LOGGER.debug("Campaign data validated successfully");
  }

  /**
   * Retrieves all campaigns and converts them to DTOs.
   *
   * @return A list of {@link CampaignDTO} representing all campaigns.
   */
  @Transactional(readOnly = true)
  public List<CampaignDTO> getAllCampaigns() {
    LOGGER.debug("Retrieving all campaigns");
    Collection<Campaign> campaigns = campaignRepository.findAll();
    return campaignMapper.mapToDTO(campaigns).stream().toList();
  }

  /**
   * Counts the total number of campaigns available.
   *
   * @return The total count of campaigns.
   */
  @Transactional(readOnly = true)
  public long countAllCampaigns() {
    LOGGER.debug("Counting all campaigns");
    return campaignRepository.count();
  }

  /**
   * Attempts to find an active campaign with the highest bid for a given category at the time of
   * the request. If no campaign is found for the specified {@link Category}, it retries the search
   * without considering the category.
   *
   * @param category The category to filter campaigns, or null to ignore the category.
   * @return The campaign with the highest bid.
   * @throws NoSuchElementException if no active campaign is found after retries.
   */
  @Transactional(readOnly = true)
  public Campaign getActiveCampaignWithHighestBid(Category category) {
    Instant requestTime = Instant.now();
    try {
      return findActiveCampaignWithHighestBid(category, requestTime);
    } catch (NoSuchElementException e) {
      LOGGER.debug(
          "No campaign found for category {}. Trying without category constraint.", category);
      if (category != null) {
        // Retry without category
        return findActiveCampaignWithHighestBid(null, requestTime);
      } else {
        // If the retry was already without category, rethrow the exception
        throw e;
      }
    }
  }

  /**
   * Finds an active campaign based on the provided category and current time. If no active
   * campaigns are found, a {@link NoSuchElementException} is thrown.
   *
   * @param category The category to filter campaigns, or null to fetch campaigns without category
   *     constraint.
   * @return The active campaign with the highest bid for the specified category.
   * @throws NoSuchElementException if no active campaigns are found for the given category.
   */
  @Transactional(readOnly = true)
  public Campaign findActiveCampaignWithHighestBid(Category category)
      throws NoSuchElementException {
    return findActiveCampaignWithHighestBid(category, Instant.now());
  }

  /**
   * Finds an active campaign based on the provided category and specified request time. If no
   * active campaign is found, a {@link NoSuchElementException} is thrown.
   *
   * @param category The category to filter campaigns, or null to fetch campaigns without category
   *     constraint.
   * @param requestTime The timestamp when the request was made.
   * @return The active campaign with the highest bid for the specified category.
   * @throws NoSuchElementException if no active campaigns are found for the given category and
   *     time.
   */
  @Transactional(readOnly = true)
  public Campaign findActiveCampaignWithHighestBid(Category category, Instant requestTime)
      throws NoSuchElementException {
    LOGGER.debug("Finding active campaign for category: {} at time: {}", category, requestTime);
    final Instant tenDaysAgo = requestTime.minus(Duration.ofDays(10));
    final PageRequest pageRequest = PageRequest.of(0, 1);

    final List<Campaign> activeCampaigns =
        campaignRepository
            .findAllActiveCampaignWithHighestBidByCategory(
                requestTime, tenDaysAgo, category, pageRequest)
            .getContent();

    if (activeCampaigns.isEmpty()) {
      LOGGER.debug("No active campaigns found for category: {}", category);
      throw new NoSuchElementException("No active campaigns found");
    }
    final Campaign campaign = activeCampaigns.get(0);
    LOGGER.debug("Active campaign found: {}", campaign);
    return campaign;
  }

  /**
   * Persists a campaign based on the provided {@link CampaignDTO}.
   *
   * @param campaignDTO The DTO containing campaign details.
   * @return The persisted {@link Campaign}.
   */
  @Transactional
  public Campaign persistCampaign(CampaignDTO campaignDTO) {
    LOGGER.debug("Persisting campaign: {}", campaignDTO);
    Campaign persistedCampaign = campaignRepository.save(campaignMapper.mapToModel(campaignDTO));
    LOGGER.info("Campaign persisted successfully: {}", persistedCampaign);
    return persistedCampaign;
  }

  /**
   * Persists a list of campaigns.
   *
   * @param campaigns The list of {@link Campaign} entities to be persisted.
   * @return A list of persisted {@link Campaign} entities.
   */
  @Transactional
  public List<Campaign> persistCampaigns(List<Campaign> campaigns) {
    LOGGER.debug("Persisting multiple campaigns: {}", campaigns);
    return campaignRepository.saveAll(campaigns);
  }
}
