package com.mabaya.ads.utils;

import static org.mockito.Mockito.*;

import com.mabaya.ads.service.CampaignService;
import com.mabaya.ads.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InitialDataGenerationRunnerTest {

  @Mock private ProductService productService;

  @Mock private CampaignService campaignService;

  @Spy private InitialDataGenerationRunner runnerSpy;

  @BeforeEach
  void setUp() {
    runnerSpy = new InitialDataGenerationRunner();
    runnerSpy.setProductService(productService);
    runnerSpy.setCampaignService(campaignService);
    runnerSpy = spy(runnerSpy);
  }

  @Test
  void testShouldGenerateInitialDataWhenTablesAreEmpty() {
    when(productService.countAllProducts()).thenReturn(0L);
    when(campaignService.countAllCampaigns()).thenReturn(0L);

    runnerSpy.run(null);

    verify(runnerSpy, times(1)).generateInitialData();
    verify(productService, atLeastOnce()).persistProducts(anyList());
    verify(campaignService, atLeastOnce()).persistCampaigns(anyList());
  }

  @Test
  void testShouldNotGenerateInitialDataProductsTableIsNotEmpty() {
    when(productService.countAllProducts()).thenReturn(1L);

    runnerSpy.run(null);

    verify(runnerSpy, never()).generateInitialData();
    verify(productService, never()).persistProducts(anyList());
    verify(campaignService, never()).persistCampaigns(anyList());
  }

  @Test
  void testShouldNotGenerateInitialDataCampaignsTableIsNotEmpty() {
    when(productService.countAllProducts()).thenReturn(0L);
    when(campaignService.countAllCampaigns()).thenReturn(1L);

    runnerSpy.run(null);

    verify(runnerSpy, never()).generateInitialData();
    verify(productService, never()).persistProducts(anyList());
    verify(campaignService, never()).persistCampaigns(anyList());
  }
}
