package com.enterprise.smartEcommerce.services;



import com.enterprise.smartEcommerce.dtos.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse confirmOrderAfterPayment(String email);
    List<OrderResponse> getUserOrders(String email);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrderStatus(Long orderId, String status);
}
