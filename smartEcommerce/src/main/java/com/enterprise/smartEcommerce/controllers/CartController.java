package com.enterprise.smartEcommerce.controllers;



import com.enterprise.smartEcommerce.dtos.CartRequest;
import com.enterprise.smartEcommerce.dtos.CartResponse;
import com.enterprise.smartEcommerce.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody CartRequest request, Principal principal) {
        // principal.getName() securely returns the email extracted from the JWT Token!
        return new ResponseEntity<>(cartService.addToCart(request, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CartResponse>> getUserCart(Principal principal) {
        return ResponseEntity.ok(cartService.getUserCart(principal.getName()));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartItemId, Principal principal) {
        cartService.removeFromCart(cartItemId, principal.getName());
        return ResponseEntity.ok("Item removed from cart successfully");
    }
}