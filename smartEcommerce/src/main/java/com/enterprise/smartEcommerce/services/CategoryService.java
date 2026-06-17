package com.enterprise.smartEcommerce.services;



import com.enterprise.smartEcommerce.dtos.CategoryRequest;
import com.enterprise.smartEcommerce.dtos.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
}
