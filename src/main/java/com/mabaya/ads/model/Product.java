package com.mabaya.ads.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Todo Should we create an interface for the getters? Todo Should we remove the setters?
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Entity
@Table(indexes = {@Index(name = "idx_product_category", columnList = "category")})
public class Product {

  @Id
  @SequenceGenerator(
      name = "product_sequence",
      sequenceName = "product_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
  private Long id;

  private String title;
  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  private Category category;

  @Column(unique = true)
  private String serialNumber;

  public Product() {}

  public Product(String title, BigDecimal price, Category category, String serialNumber) {
    this.title = title;
    this.price = price;
    this.category = category;
    this.serialNumber = serialNumber;
  }

  public Product(Long id, String title, BigDecimal price, Category category, String serialNumber) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.category = category;
    this.serialNumber = serialNumber;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }
}
