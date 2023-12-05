package com.mabaya.ads.repository;

import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Category;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

  // Preferred complexity rather than separate call to database.
  // Ideally would have a more experienced architect to brainstorm.
  /**
   * Finds all active campaigns with the highest bid for a given category. The query joins the
   * Campaign and Product entities, filtering by the campaign's start date to ensure it's active
   * (within the last 10 days). It then filters by category (if provided) and orders the results by
   * the campaign's bid in descending order, fetching the top result. If no category is specified,
   * it defaults to fetching campaigns regardless of the product category.
   */
  @Query(
      "SELECT c FROM Campaign c JOIN c.products p WHERE c.startDate <= :now AND c.startDate > :nowMinus10Days AND (:category IS NULL OR p.category = :category) ORDER BY c.bid DESC")
  Page<Campaign> findAllActiveCampaignWithHighestBidByCategory(
      @Param("now") Instant now,
      @Param("nowMinus10Days") Instant nowMinus10Days,
      @Param("category") Category category,
      Pageable pageable);
}
