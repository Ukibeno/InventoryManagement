package InventoryManagement.model;

import InventoryManagement.dto.OrderItemDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String productCode;

    // Constructor to map from DTO and Order
    public OrderItem(OrderItemDto itemDto, Order order, Product product) {
        this.quantity = itemDto.getQuantity();
        this.price = itemDto.getPrice();
/*
        this.product = product; // Set Product from the fetched Product entity
*/
        this.order = order;
    }


}
