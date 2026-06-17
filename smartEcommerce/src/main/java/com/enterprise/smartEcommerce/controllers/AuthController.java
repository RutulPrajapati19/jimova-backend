package com.enterprise.smartEcommerce.controllers;



import com.enterprise.smartEcommerce.dtos.AuthResponse;
import com.enterprise.smartEcommerce.dtos.LoginRequest;
import com.enterprise.smartEcommerce.dtos.RegisterRequest;
import com.enterprise.smartEcommerce.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity forgotPassword(@RequestParam String email) {
        try {
            authService.initiatePasswordReset(email);
            return ResponseEntity.ok(Map.of("message",
                    "If that email exists, a reset link has been sent."));
        } catch (Exception e) {
            // Always return 200 to prevent email enumeration attacks
            return ResponseEntity.ok(Map.of("message",
                    "If that email exists, a reset link has been sent."));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        try {
            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password reset successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
