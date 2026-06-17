package com.enterprise.smartEcommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We don't link to the Product table here, because if a product is deleted
    // from the store later, we still need the order history to show the name!
    private Long productId;
    private String productName;
    private String imageUrl;

    private Integer quantity;

    @Column(name = "price_at_purchase")
    private BigDecimal unitPrice;

    // ✦ THE FIX: Explicitly link back to the parent Order so it gets the ID instantly ✦
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}