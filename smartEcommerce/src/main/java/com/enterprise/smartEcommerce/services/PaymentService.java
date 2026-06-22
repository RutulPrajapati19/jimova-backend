package com.enterprise.smartEcommerce.services;

public interface PaymentService {
    String createCheckoutSession(String email) throws Exception;
}