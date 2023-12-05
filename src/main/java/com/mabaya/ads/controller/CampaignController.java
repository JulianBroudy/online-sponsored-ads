package com.mabaya.ads.controller;

import com.mabaya.ads.dto.CampaignDTO;
import com.mabaya.ads.service.CampaignService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for campaign-related operations. This controller handles HTTP requests for
 * managing campaigns, utilizing {@link CampaignService} for business logic.
 *
 * <p>It offers endpoints for retrieving all campaigns and creating new campaigns, ensuring proper
 * handling of HTTP requests and responses.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 * @see CampaignService
 * @see CampaignDTO
 */
@V1RestController
@RequestMapping(path = "/campaign")
public class CampaignController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CampaignController.class);

  private final CampaignService campaignService;

  @Autowired
  public CampaignController(CampaignService campaignService) {
    this.campaignService = campaignService;
  }

  /**
   * Retrieves all existing campaigns and returns them as a list of {@link CampaignDTO}.
   *
   * @return ResponseEntity containing a list of CampaignDTOs.
   */
  @GetMapping
  public ResponseEntity<List<CampaignDTO>> getCampaigns() {
    LOGGER.info("Request received to fetch all campaigns");
    try {
      final List<CampaignDTO> allExistingCampaigns = campaignService.getAllCampaigns();
      return ResponseEntity.ok(allExistingCampaigns);
    } catch (Exception e) {
      LOGGER.error("Unexpected error while fetching campaigns: {}", e.getMessage(), e);
      throw e; // Handled by GlobalExceptionHandler
    }
  }

  /**
   * Creates a new campaign based on the provided {@link CampaignDTO} and returns the created
   * campaign.
   *
   * @param campaignDTO The DTO containing the details for the new campaign.
   * @return ResponseEntity containing the created CampaignDTO.
   */
  @PostMapping
  public ResponseEntity<CampaignDTO> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO) {
    LOGGER.info("Request received to create a new campaign: {}", campaignDTO);
    try {
      final CampaignDTO createdCampaignDTO = campaignService.createCampaign(campaignDTO);
      return new ResponseEntity<>(createdCampaignDTO, HttpStatus.CREATED);
    } catch (Exception e) {
      LOGGER.error("Error while creating campaign: {}", e.getMessage(), e);
      throw e; // Handled by GlobalExceptionHandler
    }
  }
}
