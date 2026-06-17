package com.enterprise.smartEcommerce.repositories;

import com.enterprise.smartEcommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // ✅ FIXED: StartingWith — "lap" matches "Laptop", "top" does NOT
    Page<Product> findByNameStartingWithIgnoreCase(String keyword, Pageable pageable);
}