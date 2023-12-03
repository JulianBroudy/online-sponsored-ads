package com.mabaya.ads.controller;

import com.mabaya.ads.dto.CampaignDTO;
import com.mabaya.ads.service.CampaignService;
import java.util.List;
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

  private final CampaignService campaignService;

  @Autowired
  public CampaignController(CampaignService campaignService) {
    this.campaignService = campaignService;
  }

  @GetMapping
  public ResponseEntity<List<CampaignDTO>> getCampaigns() {
    try {
      final List<CampaignDTO> campaigns = campaignService.getCampaigns();
      return new ResponseEntity<>(campaigns, HttpStatus.OK);
    } catch (Exception e) {
      // TODO Handle different types of exceptions with more specific messages
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  public ResponseEntity<CampaignDTO> createCampaign(@RequestBody CampaignDTO campaignDTO) {
    try {
      final CampaignDTO createdCampaign = campaignService.createCampaign(campaignDTO);
      return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
    } catch (Exception e) {
      e.printStackTrace(); // TODO Move exceptions to LOGGERs.
      // TODO Handle different types of exceptions with more specific messages
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}
