package com.enterprise.smartEcommerce.dtos;



import lombok.Data;

@Data
public class CartRequest {
    private Long productId;
    private Integer quantity;
}
