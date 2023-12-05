package com.mabaya.ads.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;

/**
 * Todo Should we create an interface for the getters? Todo Should we remove the setters?
 *
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Entity
@Table(indexes = {@Index(name = "idx_campaign_start_date", columnList = "start_date")})
public class Campaign {
  @Id
  @SequenceGenerator(
      name = "campaign_sequence",
      sequenceName = "campaign_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campaign_sequence")
  private Long id;

  private String name;
  private Instant startDate;
  private BigDecimal bid;

  @ManyToMany(fetch = FetchType.LAZY)
  private Collection<Product> products;

  public Campaign() {}

  public Campaign(String name, Instant startDate, BigDecimal bid, Collection<Product> products) {
    this.name = name;
    this.startDate = startDate;
    this.bid = bid;
    this.products = products;
  }

  public Campaign(
      Long id, String name, Instant startDate, BigDecimal bid, Collection<Product> products) {
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.bid = bid;
    this.products = products;
  }

  public Campaign(String name, Instant startDate, BigDecimal bid) {
    this.name = name;
    this.startDate = startDate;
    this.bid = bid;
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

  public Instant getStartDate() {
    return startDate;
  }

  public void setStartDate(Instant startDate) {
    this.startDate = startDate;
  }

  public BigDecimal getBid() {
    return bid;
  }

  public void setBid(BigDecimal bid) {
    this.bid = bid;
  }

  public Collection<Product> getProducts() {
    return products;
  }

  public void setProducts(Collection<Product> products) {
    this.products = products;
  }

}
