package com.enterprise.smartEcommerce.service;



import java.util.Map;

public interface PaymentService {
    Map<String, String> createCheckoutSession(String email) throws Exception;
}
