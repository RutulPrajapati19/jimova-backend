package com.enterprise.smartEcommerce.controllers;

import com.enterprise.smartEcommerce.dtos.PageResponse;
import com.enterprise.smartEcommerce.dtos.ProductRequest;
import com.enterprise.smartEcommerce.dtos.ProductResponse;
import com.enterprise.smartEcommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ── CREATE ─────────────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    // ── IMAGE UPLOAD (Cloudinary multipart) ────────────────────────────────────
    @PostMapping("/{productId}/image")
    public ResponseEntity<?> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image) {
        try {
            ProductResponse updatedProduct = productService.uploadProductImage(productId, image);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── READ ALL (with search + pagination) ────────────────────────────────────
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir, keyword));
    }

    // ── READ ONE ───────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ── READ BY CATEGORY ───────────────────────────────────────────────────────
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponse<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir));
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request) {
        try {
            ProductResponse updated = productService.updateProduct(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}