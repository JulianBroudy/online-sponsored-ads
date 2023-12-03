package com.mabaya.ads.repository;

import com.mabaya.ads.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {}
