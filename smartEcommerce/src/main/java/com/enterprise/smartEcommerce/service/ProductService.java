package com.enterprise.smartEcommerce.service;


import com.enterprise.smartEcommerce.dtos.PageResponse;
import com.enterprise.smartEcommerce.dtos.ProductRequest;
import com.enterprise.smartEcommerce.dtos.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse uploadProductImage(Long productId, MultipartFile image);
    PageResponse<ProductResponse> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword);
    ProductResponse getProductById(Long id);
    PageResponse<ProductResponse> getProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}