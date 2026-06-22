package com.enterprise.smartEcommerce.controllers;

import com.enterprise.smartEcommerce.entities.User;
import com.enterprise.smartEcommerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(Map.of(
                "name",   user.getName(),
                "email",  user.getEmail(),
                "mobile", user.getMobile() != null ? user.getMobile() : ""
        ));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            Principal principal,
            @RequestBody Map<String, String> body) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String name = body.get("name");
        if (name != null && !name.isBlank()) {
            user.setName(name.trim());
        }

        String mobile = body.get("mobile");
        if (mobile != null) {
            user.setMobile(mobile.trim());
        }

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "name",   user.getName(),
                "email",  user.getEmail(),
                "mobile", user.getMobile() != null ? user.getMobile() : ""
        ));
    }
}