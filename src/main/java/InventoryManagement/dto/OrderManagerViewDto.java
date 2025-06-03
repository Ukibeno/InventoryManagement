package InventoryManagement.dto;

import InventoryManagement.model.Status;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Order DTO for Manager view with less info (no supplier).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderManagerViewDto {
    private Long id;
    private String orderNumber;
    private CategoryDto category;
    private List<OrderItemDto> items;
    private Status status;
    private String reviewMessage;
    private BigDecimal total;
}
