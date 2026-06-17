package com.enterprise.smartEcommerce.services;

import com.enterprise.smartEcommerce.dtos.AuthResponse;
import com.enterprise.smartEcommerce.dtos.LoginRequest;
import com.enterprise.smartEcommerce.dtos.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void initiatePasswordReset(String email);
    void resetPassword(String token, String newPassword);
}