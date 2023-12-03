package com.mabaya.ads.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Todo Should we create an interface for the getters? Todo Should we remove the setters?
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Entity
@Table
public class Campaign {
  @Id private Long id;
  private String name;
  private Timestamp startDate;
  private BigDecimal bid;
  private List<Product> products;

  public Campaign() {}

  public Campaign(String name, Timestamp startDate, BigDecimal bid, List<Product> products) {
    this.name = name;
    this.startDate = startDate;
    this.bid = bid;
    this.products = products;
  }

  public Campaign(
      Long id, String name, Timestamp startDate, BigDecimal bid, List<Product> products) {
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.bid = bid;
    this.products = products;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Timestamp getStartDate() {
    return startDate;
  }

  public void setStartDate(Timestamp startDate) {
    this.startDate = startDate;
  }

  public BigDecimal getBid() {
    return bid;
  }

  public void setBid(BigDecimal bid) {
    this.bid = bid;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public boolean isActive() {
    return Duration.between(this.startDate.toInstant(), Instant.now()).toDays() < 10;
  }
}
