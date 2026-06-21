package com.enterprise.smartEcommerce.service;



import com.enterprise.smartEcommerce.dtos.CartRequest;
import com.enterprise.smartEcommerce.dtos.CartResponse;
import java.util.List;

public interface CartService {
    CartResponse addToCart(String email, CartRequest request);
    List<CartResponse> getCartItems(String email);
    void removeFromCart(String email, Long cartItemId);
    void clearCart(String email);
}
