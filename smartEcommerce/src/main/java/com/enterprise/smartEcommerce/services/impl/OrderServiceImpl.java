package com.enterprise.smartEcommerce.services.impl;

import com.enterprise.smartEcommerce.dtos.OrderItemResponse;
import com.enterprise.smartEcommerce.dtos.OrderResponse;
import com.enterprise.smartEcommerce.entities.CartItem;
import com.enterprise.smartEcommerce.entities.Order;
import com.enterprise.smartEcommerce.entities.OrderItem;
import com.enterprise.smartEcommerce.entities.User;
import com.enterprise.smartEcommerce.repositories.CartItemRepository;
import com.enterprise.smartEcommerce.repositories.OrderRepository;
import com.enterprise.smartEcommerce.repositories.UserRepository;
import com.enterprise.smartEcommerce.services.EmailService;
import com.enterprise.smartEcommerce.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public OrderResponse confirmOrderAfterPayment(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot process order. Cart is already empty.");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        Order order = Order.builder()
                .user(user)
                .customerName(user.getName())
                .customerEmail(user.getEmail())
                .totalAmount(totalAmount)
                .status("COMPLETED")
                .orderDate(LocalDateTime.now(ZoneId.of("Asia/Kolkata")))
                .shippingAddress("Pending / Provided via Stripe")
                .build();

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> OrderItem.builder()
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .imageUrl(cartItem.getProduct().getImageUrl())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getProduct().getPrice())
                .order(order)
                .build()
        ).collect(Collectors.toList());

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        try {
            emailService.sendOrderConfirmation(user.getEmail(), savedOrder);
        } catch (Exception e) {
            System.err.println("Failed to send email, but order was saved: " + e.getMessage());
        }

        return mapToResponse(savedOrder, "Order confirmed successfully! Receipt emailed.");
    }

    @Override
    @Transactional
    public List<OrderResponse> getUserOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(order -> mapToResponse(order, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> mapToResponse(order, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setStatus(status.toUpperCase());
        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder, "Order status updated successfully!");
    }

    private OrderResponse mapToResponse(Order order, String message) {
        List<OrderItemResponse> itemResponses = order.getItems() != null ?
                order.getItems().stream().map(item -> OrderItemResponse.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .imageUrl(item.getImageUrl())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build()
                ).collect(Collectors.toList()) : List.of();

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .shippingAddress(order.getShippingAddress())
                .message(message)
                .items(itemResponses)
                .build();
    }
}
