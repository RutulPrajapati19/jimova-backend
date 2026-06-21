package com.enterprise.smartEcommerce.service.ServiceImpl;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.enterprise.smartEcommerce.dtos.PageResponse;
import com.enterprise.smartEcommerce.dtos.ProductRequest;
import com.enterprise.smartEcommerce.dtos.ProductResponse;
import com.enterprise.smartEcommerce.entities.Category;
import com.enterprise.smartEcommerce.entities.Product;
import com.enterprise.smartEcommerce.repositories.CategoryRepository;
import com.enterprise.smartEcommerce.repositories.ProductRepository;
import com.enterprise.smartEcommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Cloudinary cloudinary;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                .category(category)
                .build();

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse uploadProductImage(Long productId, MultipartFile image) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        try {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            product.setImageUrl((String) uploadResult.get("secure_url"));
            return mapToResponse(productRepository.save(product));
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> page;
        if (keyword != null && !keyword.isEmpty()) {
            page = productRepository.findByNameStartsWithIgnoreCase(keyword, pageable);
        } else {
            page = productRepository.findAll(pageable);
        }

        return mapToPageResponse(page);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    @Override
    public PageResponse<ProductResponse> getProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByCategoryId(categoryId, pageable);
        return mapToPageResponse(page);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private PageResponse<ProductResponse> mapToPageResponse(Page<Product> page) {
        List<ProductResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PageResponse.<ProductResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();
    }
}
