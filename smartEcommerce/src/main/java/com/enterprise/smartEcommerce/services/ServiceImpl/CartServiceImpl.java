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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse addToCart(CartRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId());

        CartItem cartItem;
        if (existing.isPresent()) {
            cartItem = existing.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
        }

        return mapToResponse(cartItemRepository.save(cartItem));
    }

    @Override
    public List<CartResponse> getUserCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartItemRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void removeFromCart(Long cartItemId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> items = cartItemRepository.findByUserId(user.getId());
        cartItemRepository.deleteAll(items);
    }

    private CartResponse mapToResponse(CartItem cartItem) {
        BigDecimal unitPrice = cartItem.getProduct().getPrice();
        BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(cartItem.getQuantity()));

        return CartResponse.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .imageUrl(cartItem.getProduct().getImageUrl())
                .quantity(cartItem.getQuantity())
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .build();
    }
}