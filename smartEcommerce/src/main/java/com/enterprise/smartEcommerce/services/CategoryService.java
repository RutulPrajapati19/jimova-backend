package com.enterprise.smartEcommerce.services;



import com.enterprise.smartEcommerce.entities.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category createCategory(String name);
}
