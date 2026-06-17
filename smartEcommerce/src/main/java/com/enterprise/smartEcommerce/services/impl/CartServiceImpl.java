package com.enterprise.smartEcommerce.services.impl;

import com.enterprise.smartEcommerce.dtos.CartRequest;
import com.enterprise.smartEcommerce.dtos.CartResponse;
import com.enterprise.smartEcommerce.entities.CartItem;
import com.enterprise.smartEcommerce.entities.Product;
import com.enterprise.smartEcommerce.entities.User;
import com.enterprise.smartEcommerce.repositories.CartItemRepository;
import com.enterprise.smartEcommerce.repositories.ProductRepository;
import com.enterprise.smartEcommerce.repositories.UserRepository;
import com.enterprise.smartEcommerce.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CartResponse addToCart(CartRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId());

        CartItem savedItem;
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            savedItem = cartItemRepository.save(cartItem);
            // Re-attach product since it may be a proxy
            savedItem.setProduct(product);
        } else {
            CartItem newItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            savedItem = cartItemRepository.save(newItem);
        }

        return mapToResponse(savedItem);
    }

    @Override
    @Transactional
    public List<CartResponse> getUserCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartItemRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeFromCart(Long cartItemId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to delete this item");
        }

        cartItemRepository.delete(cartItem);
    }

    private CartResponse mapToResponse(CartItem cartItem) {
        // Fetch product directly to avoid lazy loading proxy issues
        Product product = cartItem.getProduct();
        BigDecimal unitPrice = product.getPrice();
        BigDecimal quantity = new BigDecimal(cartItem.getQuantity());

        return CartResponse.builder()
                .cartItemId(cartItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .quantity(cartItem.getQuantity())
                .unitPrice(unitPrice)
                .totalPrice(unitPrice.multiply(quantity))
                .imageUrl(product.getImageUrl())
                .build();
    }
}