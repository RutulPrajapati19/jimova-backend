package com.enterprise.smartEcommerce.dtos;



import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    @NotBlank(message = "Order status is required")
    private String status;
}
