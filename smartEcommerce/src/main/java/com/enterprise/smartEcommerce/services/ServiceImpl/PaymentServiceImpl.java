package com.enterprise.smartEcommerce.services.ServiceImpl;

import com.enterprise.smartEcommerce.entities.CartItem;
import com.enterprise.smartEcommerce.entities.User;
import com.enterprise.smartEcommerce.repositories.CartItemRepository;
import com.enterprise.smartEcommerce.repositories.UserRepository;
import com.enterprise.smartEcommerce.services.PaymentService;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Value("${FRONTEND_URL:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public Map<String, String> createCheckoutSession(String email) throws Exception {
        Stripe.apiKey = stripeSecretKey;

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<SessionCreateParams.LineItem> lineItems = cartItems.stream().map(item ->
                SessionCreateParams.LineItem.builder()
                        .setQuantity((long) item.getQuantity())
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(item.getProduct().getPrice().multiply(new java.math.BigDecimal("100")).longValue())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(item.getProduct().getName())
                                        .build())
                                .build())
                        .build()
        ).toList();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/order-success")
                .setCancelUrl(frontendUrl + "/cart")
                .addAllLineItem(lineItems)
                .putMetadata("userEmail", email)
                .build();

        Session session = Session.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("url", session.getUrl());
        return response;
    }
}