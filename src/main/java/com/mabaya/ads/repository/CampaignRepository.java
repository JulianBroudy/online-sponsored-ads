package com.mabaya.ads.repository;

import com.mabaya.ads.model.Campaign;
import com.mabaya.ads.model.Category;
import java.sql.Timestamp;
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
  @Query(
      "SELECT c FROM Campaign c JOIN c.products p WHERE c.startDate <= :now AND c.startDate > :nowMinus10Days AND (:category IS NULL OR p.category = :category) ORDER BY c.bid DESC")
  Page<Campaign> findAllActiveCampaignWithHighestBidByCategory(
      @Param("now") Timestamp now,
      @Param("nowMinus10Days") Timestamp nowMinus10Days,
      @Param("category") Category category,
      Pageable pageable);
}
