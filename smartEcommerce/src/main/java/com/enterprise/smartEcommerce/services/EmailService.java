package com.enterprise.smartEcommerce.services;



import com.enterprise.smartEcommerce.entities.Order;

public interface EmailService {
    void sendOrderConfirmation(String toEmail, Order order);
    void sendPasswordResetEmail(String to, String resetLink);
}
