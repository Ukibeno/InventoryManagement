package InventoryManagement.dto;



import InventoryManagement.model.Status;
import InventoryManagement.model.Supplier;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto  {
    private Long               id;
    private String             orderNumber;
    private UserDto            user;
    private CategoryDto        categoryDto;
    private List<OrderItemDto> items;
    private Status             status;
    private SupplierDto        supplier; // null until assigned
    private BigDecimal         total;
}
