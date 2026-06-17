package com.enterprise.smartEcommerce.services.impl;

import com.enterprise.smartEcommerce.entities.CartItem;
import com.enterprise.smartEcommerce.entities.Product;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    // ✅ Reads from environment variable — never hardcoded
    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;

    // ✅ Reads frontend URL from env var — works both locally and on Vercel
    @Value("${FRONTEND_URL:http://localhost:5173}")
    private String frontendUrl;

    @Override
    @Transactional
    public String createCheckoutSession(String email) {
        Stripe.apiKey = stripeSecretKey;

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot checkout with an empty cart");
        }

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            long amountInCents = product.getPrice().multiply(new BigDecimal(100)).longValue();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity((long) item.getQuantity())
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("inr")
                                    .setUnitAmount(amountInCents)
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(product.getName())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
            lineItems.add(lineItem);
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                // ✅ Uses env var — works on localhost AND on Vercel live site
                .setSuccessUrl(frontendUrl + "/order-success")
                .setCancelUrl(frontendUrl + "/cart")
                .addAllLineItem(lineItems)
                .build();

        try {
            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Stripe checkout error: " + e.getMessage());
        }
    }
}