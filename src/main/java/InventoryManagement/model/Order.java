package InventoryManagement.model;

import InventoryManagement.util.OrderNumberGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity{

    // A unique identifier for the order, useful for tracking.
    @Column(nullable = false, unique = true)
    private String orderNumber;

    // The current status of the order (e.g., CREATED, EDITED, COMPLETED).
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // OrderItems serve as the detailed list of products that are low in stock.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    // The category of product items this order concerns.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Admin may assign a Supplier to this order.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    // Order created by a Manager.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public BigDecimal getTotal() {
        return items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PrePersist
    public void generateOrderNumber() {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            orderNumber = OrderNumberGenerator.generateOrderNumber();
        }
    }
}
