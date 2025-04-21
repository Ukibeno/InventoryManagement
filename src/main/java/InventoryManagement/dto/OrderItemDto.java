package InventoryManagement.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;               // For identifying an existing item (for updates)
    private Long productId;        // Refers to the Product's ID, so you can fetch the actual Product entity
    private int quantity;          // Quantity of the product in this order item
    private BigDecimal price;      // The price of the product
}
