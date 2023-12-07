package com.mabaya.ads.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.mabaya.ads.AbstractIntegrationTest;
import com.mabaya.ads.dto.CampaignDTO;
import com.mabaya.ads.dto.ExceptionResponse;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.model.Product;
import com.mabaya.ads.repository.CampaignRepository;
import com.mabaya.ads.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CampaignControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private CampaignRepository campaignRepository;

  @Autowired private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    campaignRepository.deleteAll();
    productRepository.deleteAll();
  }

  private List<Product> createProducts(int numberOfProducts, Category category) {
    return createProducts(numberOfProducts, category, 0);
  }

  private List<Product> createProducts(int numberOfProducts, Category category, int startFrom) {
    return IntStream.range(0, numberOfProducts)
        .mapToObj(
            i ->
                new Product(
                    "Product " + i,
                    BigDecimal.valueOf(100 + startFrom + i),
                    category,
                    "SN" + (startFrom + i)))
        .collect(Collectors.toList());
  }

  @Test
  public void shouldCreateCampaign() {
    List<Long> productIds =
        productRepository.saveAll(createProducts(3, Category.HEALTH)).stream()
            .map(Product::getId)
            .toList();
    CampaignDTO newCampaign =
        new CampaignDTO(
            Optional.empty(),
            "New Campaign",
            Instant.now().plus(2, ChronoUnit.DAYS),
            BigDecimal.valueOf(50.0),
            productIds);

    ResponseEntity<CampaignDTO> response =
        restTemplate.postForEntity("/api/v1/campaign", newCampaign, CampaignDTO.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("New Campaign", response.getBody().name());
  }

  @Test
  public void shouldFailForNotExistingProduct() {
    List<Long> productIds =
        new java.util.ArrayList<>(
            productRepository.saveAll(createProducts(3, Category.HEALTH)).stream()
                .map(Product::getId)
                .toList());

    productIds.addAll(List.of(40234L, 74357L));

    CampaignDTO invalidCampaign =
        new CampaignDTO(
            Optional.empty(),
            "New Campaign",
            Instant.now().plus(2, ChronoUnit.DAYS),
            BigDecimal.valueOf(50.0),
            productIds);

    ResponseEntity<ExceptionResponse> response =
        restTemplate.postForEntity("/api/v1/campaign", invalidCampaign, ExceptionResponse.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    final String errorMessage = response.getBody().errorMessage();
    assertTrue(errorMessage.contains("Products not found for IDs:"));
    assertTrue(errorMessage.contains("74357"));
    assertTrue(errorMessage.contains("40234"));
  }

  @Test
  public void shouldFailForBlankName() {
    List<Long> productIds =
        productRepository.saveAll(createProducts(3, Category.HEALTH)).stream()
            .map(Product::getId)
            .toList();
    CampaignDTO invalidCampaign =
        new CampaignDTO(
            Optional.empty(),
            " ",
            Instant.now().plus(2, ChronoUnit.DAYS),
            BigDecimal.valueOf(50.0),
            productIds);

    ResponseEntity<ExceptionResponse> response =
        restTemplate.postForEntity("/api/v1/campaign", invalidCampaign, ExceptionResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().errorMessage().contains("name: must not be blank"));
  }

  @Test
  public void shouldFailForStartDateInThePast() {
    List<Long> productIds =
        productRepository.saveAll(createProducts(3, Category.HEALTH)).stream()
            .map(Product::getId)
            .toList();
    CampaignDTO invalidCampaign =
        new CampaignDTO(
            Optional.empty(),
            "Campaign",
            Instant.now().minus(2, ChronoUnit.DAYS),
            BigDecimal.valueOf(50.0),
            productIds);

    ResponseEntity<ExceptionResponse> response =
        restTemplate.postForEntity("/api/v1/campaign", invalidCampaign, ExceptionResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().errorMessage().contains("startDate: must be a date in the present or in the future"));
  }

  @Test
  public void shouldFailForNotPositiveBid() {
    List<Long> productIds =
        productRepository.saveAll(createProducts(3, Category.HEALTH)).stream()
            .map(Product::getId)
            .toList();
    CampaignDTO invalidCampaign =
        new CampaignDTO(
            Optional.empty(),
            "Campaign",
            Instant.now().plus(2, ChronoUnit.DAYS),
            BigDecimal.valueOf(0),
            productIds);

    ResponseEntity<ExceptionResponse> response =
        restTemplate.postForEntity("/api/v1/campaign", invalidCampaign, ExceptionResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().errorMessage().contains("bid: must be greater than 0"));
  }

  @Test
  public void shouldFailForEmptyList() {
    productRepository.saveAll(createProducts(3, Category.HEALTH));
    CampaignDTO invalidCampaign =
        new CampaignDTO(
            Optional.empty(),
            "Campaign",
            Instant.now().plus(2, ChronoUnit.DAYS),
            BigDecimal.valueOf(100),
            Collections.emptyList());

    ResponseEntity<ExceptionResponse> response =
        restTemplate.postForEntity("/api/v1/campaign", invalidCampaign, ExceptionResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().errorMessage().contains("Product IDs cannot be null or empty"));
  }

  @Test
  public void shouldFailForNegativeProductId() {
    productRepository.saveAll(createProducts(3, Category.HEALTH));
    CampaignDTO invalidCampaign =
        new CampaignDTO(
            Optional.empty(),
            "Campaign",
            Instant.now().plus(2, ChronoUnit.DAYS),
            BigDecimal.valueOf(100),
            List.of(-2L));

    ResponseEntity<ExceptionResponse> response =
        restTemplate.postForEntity("/api/v1/campaign", invalidCampaign, ExceptionResponse.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().errorMessage().contains("Products not found for IDs: [-2]"));
  }

  @Test
  public void shouldFailAndShowAllValidationErrors() {
    List<Long> productIds =
        productRepository.saveAll(createProducts(3, Category.HEALTH)).stream()
            .map(Product::getId)
            .toList();
    CampaignDTO invalidCampaign =
        new CampaignDTO(
            Optional.empty(),
            "",
            Instant.now().minus(2, ChronoUnit.DAYS),
            BigDecimal.valueOf(-10.0),
            productIds);

    ResponseEntity<ExceptionResponse> response =
        restTemplate.postForEntity("/api/v1/campaign", invalidCampaign, ExceptionResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    String errorMessage = response.getBody().errorMessage();
    assertTrue(
        errorMessage.contains("name: must not be blank")); // Checking for empty name validation
    assertTrue(errorMessage.contains("startDate: must be a date in the present or in the future"));
    assertTrue(errorMessage.contains("bid: must be greater than 0"));
  }
}
