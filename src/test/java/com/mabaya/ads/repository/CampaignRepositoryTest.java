package com.mabaya.ads.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.mabaya.ads.AbstractIntegrationTest;
import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.model.Product;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// For debugging purposes remove comment
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CampaignRepositoryTest extends AbstractIntegrationTest {

  private static final String ACTIVE = "Active Campaign";
  private static final String INACTIVE = "Inactive Campaign";
  @Autowired private CampaignRepository campaignRepository;
  @Autowired private ProductRepository productRepository;
  private Instant now;
  private Instant tenDaysAgo;

  @BeforeEach
  void setUp() {
    now = Instant.now();
    tenDaysAgo = now.minus(10, ChronoUnit.DAYS);
  }

  @AfterEach
  void tearDown() {
    campaignRepository.deleteAll();
    productRepository.deleteAll();
  }

  @Test
  public void shouldReturnActiveCampaignsWithinLast10Days() {
    List<Product> products = createProducts(2, Category.BOOKS);
    productRepository.saveAll(products);

    List<Campaign> campaigns =
        List.of(
            createCampaignWithBid50(INACTIVE, tenDaysAgo.minus(5, ChronoUnit.DAYS), products),
            createCampaignWithBid50(ACTIVE, tenDaysAgo.plus(1, ChronoUnit.DAYS), products));
    campaignRepository.saveAll(campaigns);

    Page<Campaign> result =
        campaignRepository.findAllActiveCampaignsByCategoryOrderedByBid(
            now, tenDaysAgo, null, PageRequest.of(0, 10));

    assertEquals(1, result.getContent().size());
    assertTrue(
        result.getContent().stream().allMatch(campaign -> campaign.getName().equals(ACTIVE)));
  }

  @Test
  public void shouldReturnCampaignsWithSpecificCategory() {
    List<Product> products = createProducts(2, Category.BOOKS);
    productRepository.saveAll(products);

    List<Campaign> campaigns = List.of(createCampaignWithBid50(ACTIVE, now, products));
    campaignRepository.saveAll(campaigns);

    Page<Campaign> result =
        campaignRepository.findAllActiveCampaignsByCategoryOrderedByBid(
            now, tenDaysAgo, Category.BOOKS, PageRequest.of(0, 10));

    assertEquals(1, result.getContent().size());
    assertTrue(
        result.getContent().stream().allMatch(campaign -> campaign.getName().equals(ACTIVE)));
  }

  @Test
  public void shouldReturnAllActiveCampaignsWhenCategoryIsNull() {
    List<Product> products = createProducts(2, Category.BOOKS);
    productRepository.saveAll(products);

    List<Campaign> campaigns =
        List.of(
            createCampaignWithBid50(ACTIVE, now, products),
            createCampaignWithBid50(ACTIVE, now.minus(1, ChronoUnit.DAYS), products));
    campaignRepository.saveAll(campaigns);

    Page<Campaign> result =
        campaignRepository.findAllActiveCampaignsByCategoryOrderedByBid(
            now, tenDaysAgo, null, PageRequest.of(0, 10));

    assertEquals(2, result.getContent().size());
    assertTrue(
        result.getContent().stream().allMatch(campaign -> campaign.getName().equals(ACTIVE)));
  }

  @Test
  public void shouldOrderCampaignsByBidDescending() {
    List<Product> products = createProducts(2, Category.BOOKS);
    productRepository.saveAll(products);

    List<Campaign> campaigns =
        List.of(
            createCampaign("Low Bid Campaign", now, new BigDecimal("50.00"), products),
            createCampaign("High Bid Campaign", now, new BigDecimal("100.00"), products));
    campaignRepository.saveAll(campaigns);

    Page<Campaign> result =
        campaignRepository.findAllActiveCampaignsByCategoryOrderedByBid(
            now, tenDaysAgo, null, PageRequest.of(0, 10));

    assertEquals(2, result.getContent().size());
    assertEquals("High Bid Campaign", result.getContent().get(0).getName());
    assertEquals("Low Bid Campaign", result.getContent().get(1).getName());
  }

  @Test
  public void shouldNotIncludeCampaignStartingExactly10DaysAgo() {
    List<Product> products = createProducts(2, Category.ELECTRONICS);
    productRepository.saveAll(products);

    Campaign exactlyTenDaysOldCampaign = createCampaignWithBid50(INACTIVE, tenDaysAgo, products);
    campaignRepository.save(exactlyTenDaysOldCampaign);

    Page<Campaign> result =
        campaignRepository.findAllActiveCampaignsByCategoryOrderedByBid(
            now, tenDaysAgo, null, PageRequest.of(0, 10));

    assertTrue(
        result.getContent().stream().noneMatch(campaign -> campaign.getName().equals(INACTIVE)));
  }

  @Test
  public void shouldReturnCampaignForOverlappingCampaignsWithHighestBidOrder() {
    List<Product> products = createProducts(1, Category.BEAUTY);
    productRepository.saveAll(products);

    Campaign lowerBidCampaign =
        createCampaign(
            "Lower Bid Campaign", now.minus(1, ChronoUnit.DAYS), new BigDecimal("50.00"), products);
    Campaign higherBidCampaign =
        createCampaign(
            "Higher Bid Campaign",
            now.minus(2, ChronoUnit.DAYS),
            new BigDecimal("100.00"),
            products);
    campaignRepository.saveAll(List.of(lowerBidCampaign, higherBidCampaign));

    Page<Campaign> result =
        campaignRepository.findAllActiveCampaignsByCategoryOrderedByBid(
            now, tenDaysAgo, Category.BEAUTY, PageRequest.of(0, 10));

    assertEquals(2, result.getContent().size());
    assertEquals("Higher Bid Campaign", result.getContent().get(0).getName());
    assertEquals("Lower Bid Campaign", result.getContent().get(1).getName());
  }

  @Test
  public void shouldReturnCampaignsWithMultipleCategoriesIfOneMatches() {
    List<Product> mixedCategoryProducts =
        List.of(
            new Product(
                "Electronics Product", BigDecimal.valueOf(150.00), Category.ELECTRONICS, "SN100"),
            new Product("Fashion Product", BigDecimal.valueOf(200.00), Category.FASHION, "SN101"));
    productRepository.saveAll(mixedCategoryProducts);

    Campaign mixedCategoryCampaign = createCampaignWithBid50(ACTIVE, now, mixedCategoryProducts);
    campaignRepository.save(mixedCategoryCampaign);

    Page<Campaign> result =
        campaignRepository.findAllActiveCampaignsByCategoryOrderedByBid(
            now, tenDaysAgo, Category.FASHION, PageRequest.of(0, 10));

    assertEquals(1, result.getContent().size());
    assertEquals(ACTIVE, result.getContent().get(0).getName());
  }

  @Test
  public void shouldNotReturnCampaignsWithFutureStartDate() {
    List<Product> products = createProducts(2, Category.HEALTH);
    productRepository.saveAll(products);

    Campaign futureCampaign =
        createCampaignWithBid50(ACTIVE, now.plus(5, ChronoUnit.DAYS), products);
    campaignRepository.save(futureCampaign);

    Page<Campaign> result =
        campaignRepository.findAllActiveCampaignsByCategoryOrderedByBid(
            now, tenDaysAgo, null, PageRequest.of(0, 10));

    assertEquals(0, result.getContent().size());
  }

  private List<Product> createProducts(int numberOfProducts, Category category) {
    return IntStream.range(0, numberOfProducts)
        .mapToObj(i -> new Product("Product " + i, BigDecimal.valueOf(100 + i), category, "SN" + i))
        .collect(Collectors.toList());
  }

  private Campaign createCampaignWithBid50(String name, Instant startDate, List<Product> products) {
    return new Campaign(name, startDate, BigDecimal.valueOf(50.00), products);
  }

  private Campaign createCampaign(
      String name, Instant startDate, BigDecimal bid, List<Product> products) {
    return new Campaign(name, startDate, bid, products);
  }
}
