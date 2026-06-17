package com.enterprise.smartEcommerce.repositories;



import com.enterprise.smartEcommerce.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Find everything in a specific user's cart
    List<CartItem> findByUserId(Long userId);

    // Check if a specific product is already in a specific user's cart
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    // Clear the cart completely (used after they successfully pay for an order)
    void deleteByUserId(Long userId);
}