package com.enterprise.smartEcommerce.controllers;

import com.enterprise.smartEcommerce.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor

public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(Authentication authentication) {
        try {
            // Security Check: Ensure the user is logged in
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(403).body(Map.of("error", "Unauthorized. Please log in."));
            }

            // Extract the email from the JWT token and pass it to the service
            String email = authentication.getName();
            String checkoutUrl = paymentService.createCheckoutSession(email);

            return ResponseEntity.ok(Map.of("checkoutUrl", checkoutUrl));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}