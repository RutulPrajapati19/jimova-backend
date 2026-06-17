package com.enterprise.smartEcommerce.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Long categoryId;
    private String imageUrl;   // ✅ used by admin panel — paste Cloudinary URL directly
}