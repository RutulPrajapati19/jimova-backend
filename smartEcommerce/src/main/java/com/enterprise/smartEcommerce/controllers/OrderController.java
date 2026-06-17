package com.enterprise.smartEcommerce.controllers;

import com.enterprise.smartEcommerce.dtos.OrderResponse;
import com.enterprise.smartEcommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 1. Confirm order after Stripe redirects to success page
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmOrder(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }

        try {
            String email = authentication.getName();

            // 👇 Here is where we call the newly renamed method! 👇
            OrderResponse response = orderService.confirmOrderAfterPayment(email);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2. Get orders for the logged-in user
    @GetMapping("/my-orders")
    public ResponseEntity<?> getUserOrders(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }

        try {
            String email = authentication.getName();
            List<OrderResponse> orders = orderService.getUserOrders(email);
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ---> ADMIN ENDPOINTS <---

    // 3. Get all orders (Admin only - protected by SecurityConfig)
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // 4. Update order status (Admin only)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        try {
            OrderResponse response = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}