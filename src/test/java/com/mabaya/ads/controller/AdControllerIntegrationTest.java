package com.mabaya.ads.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.mabaya.ads.AbstractIntegrationTest;
import com.mabaya.ads.dto.ExceptionResponse;
import com.mabaya.ads.dto.ProductDTO;
import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.model.Product;
import com.mabaya.ads.repository.CampaignRepository;
import com.mabaya.ads.repository.ProductRepository;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/**
 * Todo ExplainTheClass.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AdControllerIntegrationTest extends AbstractIntegrationTest {

  private static final String ACTIVE = "Active Campaign";
  private static final String INACTIVE = "Inactive Campaign";
  @Autowired private TestRestTemplate restTemplate;
  @Autowired private CampaignRepository campaignRepository;
  @Autowired private ProductRepository productRepository;
  private Instant now;
  private Instant tenDaysAgo;

  @BeforeEach
  void setUp() {
    campaignRepository.deleteAll();
    productRepository.deleteAll();
    now = Instant.now();
    tenDaysAgo = now.minus(10, ChronoUnit.DAYS);
  }

  @AfterEach
  void tearDown() {
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

  private Campaign createCampaign(
      String name, Instant startDate, BigDecimal bid, List<Product> products) {
    return new Campaign(name, startDate, bid, products);
  }

  @Test
  public void shouldReturnProductFromActiveCampaignNotByCategory() {
    List<Product> products = createProducts(2, Category.BOOKS);
    productRepository.saveAll(products);
    List<Product> badProducts = createProducts(2, Category.FASHION, 2);
    productRepository.saveAll(badProducts);

    List<Campaign> campaigns =
        List.of(
            createCampaign(
                INACTIVE, tenDaysAgo.minus(5, ChronoUnit.DAYS), BigDecimal.TEN, products),
            createCampaign(
                ACTIVE, tenDaysAgo.plus(1, ChronoUnit.DAYS), BigDecimal.ONE, badProducts));
    campaignRepository.saveAll(campaigns);

    ResponseEntity<ProductDTO> response =
        restTemplate.getForEntity("/api/v1/ad/BOOKS", ProductDTO.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(Category.FASHION, response.getBody().category());
    assertEquals(0, BigDecimal.valueOf(103).compareTo(response.getBody().price()));
  }

  @Test
  public void shouldReturnProductFromActiveCampaignByCategory() {
    List<Product> products = createProducts(2, Category.BOOKS);
    productRepository.saveAll(products);
    List<Product> badProducts = createProducts(2, Category.FASHION, 2);
    productRepository.saveAll(badProducts);

    List<Campaign> campaigns =
        List.of(
            createCampaign(
                INACTIVE, tenDaysAgo.minus(5, ChronoUnit.DAYS), BigDecimal.TEN, badProducts),
            createCampaign(ACTIVE, tenDaysAgo.plus(1, ChronoUnit.DAYS), BigDecimal.ONE, products));
    campaignRepository.saveAll(campaigns);

    ResponseEntity<ProductDTO> response =
        restTemplate.getForEntity("/api/v1/ad/BOOKS", ProductDTO.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(Category.BOOKS, response.getBody().category());
    assertEquals(0, BigDecimal.valueOf(101).compareTo(response.getBody().price()));
  }

  @Test
  public void shouldReturnProductFromActiveCampaignByCategoryCaseInsensitive() {
    List<Product> products = createProducts(2, Category.BOOKS);
    productRepository.saveAll(products);
    List<Product> badProducts = createProducts(2, Category.FASHION, 2);
    productRepository.saveAll(badProducts);

    List<Campaign> campaigns =
        List.of(
            createCampaign(
                INACTIVE, tenDaysAgo.minus(5, ChronoUnit.DAYS), BigDecimal.TEN, badProducts),
            createCampaign(ACTIVE, tenDaysAgo.plus(1, ChronoUnit.DAYS), BigDecimal.ONE, products));
    campaignRepository.saveAll(campaigns);

    ResponseEntity<ProductDTO> response =
        restTemplate.getForEntity("/api/v1/ad/BookS", ProductDTO.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(Category.BOOKS, response.getBody().category());
    assertEquals(0, BigDecimal.valueOf(101).compareTo(response.getBody().price()));
  }

  @Test
  public void shouldReturnNotFoundResponse() {
    List<Product> products = createProducts(2, Category.BOOKS);
    productRepository.saveAll(products);
    List<Product> badProducts = createProducts(2, Category.FASHION, 2);
    productRepository.saveAll(badProducts);

    List<Campaign> campaigns =
        List.of(
            createCampaign(INACTIVE, now.plus(5, ChronoUnit.DAYS), BigDecimal.TEN, products),
            createCampaign(
                ACTIVE, tenDaysAgo.minus(5, ChronoUnit.DAYS), BigDecimal.ONE, badProducts));
    campaignRepository.saveAll(campaigns);

    ResponseEntity<ExceptionResponse> response =
        restTemplate.getForEntity("/api/v1/ad/BOOKS", ExceptionResponse.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Not Found", response.getBody().errorCode());
  }

  @Test
  public void shouldFail() {
    ResponseEntity<ExceptionResponse> response =
        restTemplate.getForEntity("/api/v1/ad/INVALID_CATEGORY", ExceptionResponse.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Bad Request", response.getBody().errorCode());
  }
}
