package InventoryManagement.dto;

import lombok.*;

/**
 * Composite DTO that wraps the order number, category details, and order details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequest {
    private CategoryDto category;
    private OrderDto order;
}
