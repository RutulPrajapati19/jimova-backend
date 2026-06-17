package com.enterprise.smartEcommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private LocalDateTime orderDate;
    private String message;

    // ✦ THIS IS THE MISSING PIECE ✦
    private List<OrderItemResponse> items;
}