package com.enterprise.smartEcommerce.service;



import com.enterprise.smartEcommerce.entities.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category createCategory(String name);
}
