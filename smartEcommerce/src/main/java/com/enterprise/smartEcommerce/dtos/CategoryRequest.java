package com.enterprise.smartEcommerce.dtos;



import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Category name cannot be blank")
    private String name;

    private String description;
}
