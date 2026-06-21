package com.enterprise.smartEcommerce.services.impl;

import com.enterprise.smartEcommerce.entities.Order;
import com.enterprise.smartEcommerce.entities.OrderItem;
import com.enterprise.smartEcommerce.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    @Async
    @Override
    public void sendOrderConfirmation(String toEmail, Order order) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Hi ").append(order.getCustomerName()).append(",\n\n");
            sb.append("Thank you for shopping at Jimova! Your order has been confirmed.\n\n");
            sb.append("Order #").append(order.getId()).append("\n");
            sb.append("Date: ").append(order.getOrderDate()).append("\n");
            sb.append("Status: ").append(order.getStatus()).append("\n\n");
            sb.append("─────────────────────────────\n");
            sb.append("ITEMS ORDERED\n");
            sb.append("─────────────────────────────\n");

            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    BigDecimal lineTotal = item.getUnitPrice()
                            .multiply(new BigDecimal(item.getQuantity()));
                    sb.append("• ").append(item.getProductName())
                            .append(" x").append(item.getQuantity())
                            .append(" — $").append(lineTotal.toPlainString())
                            .append("\n");
                }
            }

            sb.append("─────────────────────────────\n");
            sb.append("TOTAL: $").append(order.getTotalAmount()).append("\n\n");
            sb.append("Thank you for shopping with Jimova.\n");
            sb.append("— The Jimova Team");

            sendEmail(toEmail, order.getCustomerName(),
                    "Jimova — Order #" + order.getId() + " Confirmed",
                    sb.toString());

            System.out.println("✅ Order confirmation email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            String text = "Hi,\n\n" +
                    "We received a request to reset your Jimova account password.\n\n" +
                    "Click the link below to reset your password (valid for 30 minutes):\n\n" +
                    resetLink + "\n\n" +
                    "If you didn't request this, you can safely ignore this email.\n\n" +
                    "— The Jimova Team";

            sendEmail(to, "Jimova User", "Jimova — Reset Your Password", text);
        } catch (Exception e) {
            System.err.println("❌ Failed to send reset email: " + e.getMessage());
        }
    }

    private void sendEmail(String toEmail, String toName, String subject, String textContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", "Jimova", "email", "noreply@jimova.com"));
        body.put("to", List.of(Map.of("email", toEmail, "name", toName != null ? toName : toEmail)));
        body.put("subject", subject);
        body.put("textContent", textContent);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(BREVO_URL, request, String.class);
    }
}