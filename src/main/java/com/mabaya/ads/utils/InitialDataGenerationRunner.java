package com.mabaya.ads.utils;

import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Category;
import com.mabaya.ads.model.Product;
import com.mabaya.ads.service.CampaignService;
import com.mabaya.ads.service.ProductService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Initial data generation.
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Component
public class InitialDataGenerationRunner implements ApplicationRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(InitialDataGenerationRunner.class);

  private static final int BATCH_SIZE = 50; // Adjust based on your performance needs
  private static final Map<Category, List<String>> CATEGORY_PRODUCT_NAMES;

  static {
    CATEGORY_PRODUCT_NAMES =
        Map.of(
            Category.ELECTRONICS,
            Arrays.asList(
                "Smartphone",
                "Laptop",
                "Tablet",
                "Headphones",
                "Camera",
                "Smartwatch",
                "Gaming Console",
                "Bluetooth Speaker",
                "Wireless Earbuds",
                "Drone"),
            Category.FASHION,
            Arrays.asList(
                "Dress",
                "Jeans",
                "Sunglasses",
                "T-Shirt",
                "Shoes",
                "Handbag",
                "Wristwatch",
                "Scarf",
                "Hats",
                "Jacket"),
            Category.HOMEGOODS,
            Arrays.asList(
                "Sofa",
                "Coffee Table",
                "Kitchen Blender",
                "Lamp",
                "Dining Table",
                "Bedding Set",
                "Curtains",
                "Cookware Set",
                "Vacuum Cleaner",
                "Pillow"),
            Category.BEAUTY,
            Arrays.asList(
                "Lipstick",
                "Shampoo",
                "Face Mask Set",
                "Perfume",
                "Foundation",
                "Nail Polish",
                "Hair Dryer",
                "Cologne",
                "Facial Cleanser",
                "Mascara"),
            Category.HEALTH,
            Arrays.asList(
                "Vitamins",
                "Protein Powder",
                "Multivitamins",
                "Weight Scale",
                "Fitness Tracker",
                "Health Supplements",
                "Yoga Mat",
                "Resistance Bands",
                "Treadmill",
                "Massage Gun"),
            Category.SPORTS,
            Arrays.asList(
                "Running Shoes",
                "Basketball",
                "Bicycle",
                "Yoga Mat",
                "Gym Bag",
                "Tennis Racket",
                "Fitness Gloves",
                "Sports Watch",
                "Soccer Ball",
                "Boxing Gloves"),
            Category.TRAVEL,
            Arrays.asList(
                "Luggage Set",
                "Travel Backpack",
                "Travel Adapter",
                "Travel Pillow",
                "Suitcase",
                "Neck Pillow",
                "Travel Organizer",
                "Travel Toiletry Kit",
                "Hiking Backpack",
                "Passport Holder"),
            Category.BOOKS,
            Arrays.asList(
                "Novel",
                "Cookbook",
                "Self-Help Book",
                "Mystery Novel",
                "Biography",
                "Science Fiction Book",
                "History Book",
                "Fantasy Novel",
                "Romance Novel",
                "Poetry Collection"),
            Category.PETS,
            Arrays.asList(
                "Dog Food",
                "Cat Toy",
                "Pet Bed",
                "Pet Carrier",
                "Fish Tank",
                "Bird Cage",
                "Dog Leash",
                "Cat Litter Box",
                "Dog Collar",
                "Hamster Wheel"),
            Category.OFFICE,
            Arrays.asList(
                "Notebook",
                "Pens",
                "Stapler",
                "Desk Organizer",
                "Laptop Bag",
                "Whiteboard",
                "Desk Lamp",
                "File Organizer",
                "Calculator",
                "Paper Shredder"));
  }

  private final Map<String, Integer> productNameCount = new HashMap<>();
  private final Map<Category, Integer> categoryCount = new HashMap<>();
  private ProductService productService;
  private CampaignService campaignService;

  public InitialDataGenerationRunner() {}

  @Autowired
  public void setProductService(ProductService productService) {
    this.productService = productService;
  }

  @Autowired
  public void setCampaignService(CampaignService campaignService) {
    this.campaignService = campaignService;
  }

  @Override
  public void run(ApplicationArguments args) {
    LOGGER.info("Starting initial data generation process");
    if (shouldGenerateInitialData()) {
      LOGGER.debug("Data initialization required");
      generateInitialData();
    } else {
      LOGGER.info("Data initialization not required - existing data found");
    }
    LOGGER.info("Initial data generation process completed");
  }

  private boolean shouldGenerateInitialData() {
    boolean isInitialDataGenerationRequired =
        productService.countAllProducts() == 0 && campaignService.countAllCampaigns() == 0;
    LOGGER.trace("Data initialization check: {}", isInitialDataGenerationRequired);
    return isInitialDataGenerationRequired;
  }

  protected void generateInitialData() {
    final List<Product> allProducts = generateProducts();
    generateCampaigns(allProducts);
  }

  private List<Product> generateProducts() {
    LOGGER.debug("Generating products");
    List<Product> savedProducts = new ArrayList<>();
    List<Product> batch = new ArrayList<>();
    for (int i = 1; i <= 1000; i++) {
      batch.add(generateProduct());
      if (batch.size() == BATCH_SIZE) {
        persistProductsBatch(batch, savedProducts);
      }
    }
    persistProductsBatch(batch, savedProducts); // Save remaining products
    LOGGER.debug("Finished generating products");
    return savedProducts;
  }

  private Product generateProduct() {
    Random random = new Random();
    Category category = Category.values()[random.nextInt(Category.values().length)];
    List<String> names = CATEGORY_PRODUCT_NAMES.get(category);
    String nameBase = names.get(random.nextInt(names.size()));

    int nameCount = productNameCount.getOrDefault(nameBase, 0) + 1;
    productNameCount.put(nameBase, nameCount);

    String title = nameBase + " " + nameCount;
    BigDecimal price =
        BigDecimal.valueOf(random.nextDouble() * 1000 + 100).setScale(2, RoundingMode.HALF_UP);
    String serialNumber = generateSerialNumber(category);

    LOGGER.trace("Generated product with title: {}", title);
    return new Product(title, price, category, serialNumber);
  }

  private String generateSerialNumber(Category category) {
    String categoryPrefix =
        category.name().substring(0, Math.min(3, category.name().length())).toUpperCase();

    int categoryCountValue = categoryCount.getOrDefault(category, 0) + 1;
    categoryCount.put(category, categoryCountValue);

    return categoryPrefix
        + String.format(
            "%08d", categoryCountValue); // Serial number follows the category occurrence count
  }

  private void persistProductsBatch(List<Product> batch, List<Product> savedProducts) {
    LOGGER.trace("Persisting product batch of size {}", batch.size());
    try {
      savedProducts.addAll(productService.persistProducts(batch));
      batch.clear();
      LOGGER.info("Product batch persisted successfully");
    } catch (Exception e) {
      LOGGER.error("Error persisting product batch", e);
      throw new RuntimeException("Failed to persist product batch", e);
    }
  }

  private void generateCampaigns(List<Product> allProducts) {
    LOGGER.debug("Initializing campaigns");
    List<Campaign> batch = new ArrayList<>();
    for (int i = 1; i <= 100; i++) {
      batch.add(generateCampaign(i, allProducts));
      if (batch.size() == BATCH_SIZE) {
        persistCampaignsBatch(batch);
      }
    }
    persistCampaignsBatch(batch); // Save remaining campaigns
    LOGGER.debug("Finished initializing campaigns");
  }

  private Campaign generateCampaign(int index, List<Product> allProducts) {
    LOGGER.trace("Generating campaign {}", index);
    String name = "Campaign " + index;
    Instant startDate = generateRandomStartDate();
    BigDecimal bid =
        BigDecimal.valueOf(new Random().nextDouble() * 100 + 100).setScale(2, RoundingMode.HALF_UP);

    List<Product> selectedProducts = selectRandomProducts(allProducts, new Random());
    return new Campaign(name, startDate, bid, new HashSet<>(selectedProducts));
  }

  private Instant generateRandomStartDate() {
    LOGGER.trace("Generating random start date for campaign");
    Random random = new Random();
    long millisInYear = 365L * 24 * 60 * 60 * 1000;
    long randomMillis =
        (long) (random.nextDouble() * millisInYear * 2) - millisInYear; // Range: [-1 year, +1 year]
    return Instant.now().plusMillis(randomMillis);
  }

  private List<Product> selectRandomProducts(List<Product> allProducts, Random random) {
    int numberOfProductsToAdd =
        random.nextInt(10) + 1; // For example, each campaign will have 1 to 10 products
    Collections.shuffle(allProducts);
    LOGGER.trace("Selecting {} products for a campaign", numberOfProductsToAdd);
    return allProducts.subList(0, Math.min(numberOfProductsToAdd, allProducts.size()));
  }

  private void persistCampaignsBatch(List<Campaign> batch) {
    LOGGER.trace("Persisting campaign batch of size {}", batch.size());
    try {
      campaignService.persistCampaigns(batch);
      batch.clear();
      LOGGER.info("Campaign batch persisted successfully");
    } catch (Exception e) {
      LOGGER.error("Error persisting campaign batch", e);
      throw new RuntimeException("Failed to persist product batch", e);
    }
  }
}
