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
 * {@link V1RestController} for {@link com.mabaya.ads.model.Campaign}.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
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

  @GetMapping
  public ResponseEntity<List<CampaignDTO>> getCampaigns() {
    LOGGER.info("Request to fetch campaigns");
    try {
      final List<CampaignDTO> allExistingCampaigns = campaignService.getAllCampaigns();
      return new ResponseEntity<>(allExistingCampaigns, HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.error("Unexpected error: {}", e.getMessage(), e);
      throw e;
    }
  }

  @PostMapping
  public ResponseEntity<CampaignDTO> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO) {
    LOGGER.info("Request to create campaign: {}", campaignDTO);
    try {
      final CampaignDTO createdCampaignDTO = campaignService.createCampaign(campaignDTO);
      return new ResponseEntity<>(createdCampaignDTO, HttpStatus.CREATED);
    } catch (Exception e) {
      LOGGER.error("Error creating campaign: {}", e.getMessage(), e);
      throw e; // Handled by GlobalExceptionHandler
    }
  }
}
