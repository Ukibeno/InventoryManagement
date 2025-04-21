package InventoryManagement.dto;



import InventoryManagement.model.Status;
import InventoryManagement.model.Supplier;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto  {
    private Long id;
    private String orderNumber;
    private Status status;
    private CategoryDto category;
    private List<OrderItemDto> orderItems;
    private Supplier supplier;
    private UserDto createdBy;
}
