package com.enterprise.smartEcommerce.services.impl;

import com.enterprise.smartEcommerce.entities.Order;
import com.enterprise.smartEcommerce.entities.OrderItem;
import com.enterprise.smartEcommerce.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOrderConfirmation(String toEmail, Order order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Jimova — Order #" + order.getId() + " Confirmed");

            StringBuilder sb = new StringBuilder();
            sb.append("Thank you for your order at Jimova.\n\n");
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
                            .append(" — ₹").append(lineTotal.toPlainString())
                            .append("\n");
                }
            }

            sb.append("─────────────────────────────\n");
            sb.append("TOTAL: ₹").append(order.getTotalAmount()).append("\n\n");
            sb.append("Your order will be shipped to: ").append(order.getShippingAddress()).append("\n\n");
            sb.append("Thank you for shopping with Jimova.\n");
            sb.append("— The Jimova Team");

            message.setText(sb.toString());
            mailSender.send(message);
            System.out.println("✅ Order confirmation email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send order confirmation email: " + e.getMessage());
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Jimova — Reset Your Password");
        message.setText(
                "Hi,\n\n" +
                        "We received a request to reset your Jimova account password.\n\n" +
                        "Click the link below to reset your password (valid for 30 minutes):\n\n" +
                        resetLink + "\n\n" +
                        "If you didn't request this, you can safely ignore this email.\n\n" +
                        "— The Jimova Team"
        );
        mailSender.send(message);
    }
}