package com.enterprise.smartEcommerce.services;



import com.enterprise.smartEcommerce.dtos.CartRequest;
import com.enterprise.smartEcommerce.dtos.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse addToCart(CartRequest request, String email);
    List<CartResponse> getUserCart(String email);
    void removeFromCart(Long cartItemId, String email);
}
