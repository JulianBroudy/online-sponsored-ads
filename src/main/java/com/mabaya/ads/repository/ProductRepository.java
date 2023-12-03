package com.mabaya.ads.repository;

import com.mabaya.ads.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="https://github.com/JulianBroudy">Julian Broudy</a>
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
