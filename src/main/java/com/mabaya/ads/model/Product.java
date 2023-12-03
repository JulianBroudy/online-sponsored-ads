package com.mabaya.ads.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * Todo Should we create an interface for the getters?
 * Todo Should we remove the setters?
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Entity
@Table
public class Product {

  @Id private Long id;
  private String title;
  private BigDecimal price;
  private Category category;
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
