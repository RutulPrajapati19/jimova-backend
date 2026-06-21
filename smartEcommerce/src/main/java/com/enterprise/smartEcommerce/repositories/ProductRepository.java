package com.enterprise.smartEcommerce.repositories;

import com.enterprise.smartEcommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Fixed: startsWith instead of contains
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT(:name, '%')) ORDER BY p.name ASC")
    List<Product> findByNameStartingWith(@Param("name") String name);

    // Also search by category if needed
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT(:query, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT(:query, '%')) ORDER BY p.name ASC")
    List<Product> searchProducts(@Param("query") String query);
}