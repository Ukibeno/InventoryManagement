package InventoryManagement.dto;

import lombok.*;

import java.util.List;

/**
 * Composite DTO that wraps the order number, category details, and order details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequestDto {
    private String orderNumber;
    private CategoryDto categoryDto;
    private List<OrderItemDto> items;
    private int quantity;
}
