package com.enterprise.smartEcommerce.services;

import com.enterprise.smartEcommerce.dtos.OrderResponse;
import java.util.List;

public interface OrderService {
    // Called automatically when the user lands on the /success page
    OrderResponse confirmOrderAfterPayment(String email);

    // User Methods
    List<OrderResponse> getUserOrders(String email);

    // Admin Methods
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrderStatus(Long orderId, String status);
}