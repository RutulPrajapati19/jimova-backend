package com.enterprise.smartEcommerce.services.impl;



import com.enterprise.smartEcommerce.dtos.CategoryRequest;
import com.enterprise.smartEcommerce.dtos.CategoryResponse;
import com.enterprise.smartEcommerce.entities.Category;
import com.enterprise.smartEcommerce.repositories.CategoryRepository;
import com.enterprise.smartEcommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        // 1. Check if category name already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists!");
        }

        // 2. Convert DTO to Entity
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        // 3. Save to database
        Category savedCategory = categoryRepository.save(category);

        // 4. Convert Entity back to Response DTO
        return mapToResponse(savedCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
        return mapToResponse(category);
    }

    // Helper method to convert Entity to DTO
    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
