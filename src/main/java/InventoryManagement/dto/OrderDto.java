package InventoryManagement.dto;



import InventoryManagement.dto.view.Views;
import InventoryManagement.model.Status;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto  {

    @JsonView(Views.ManagerView.class)
    private Long  id;

    @JsonView(Views.ManagerView.class)
    private String  orderNumber;

    @JsonView(Views.ManagerView.class)
    private UserDto  user;

    @JsonView(Views.ManagerView.class)
    private CategoryDto category;

    @JsonView(Views.ManagerView.class)
    private List<OrderItemDto> items;

    @JsonView(Views.ManagerView.class)
    private Status  status;

    @JsonView(Views.AdminView.class)
    private SupplierDto  supplier;

    @JsonView(Views.ManagerView.class)
    private String reviewMessage;

    @JsonView(Views.ManagerView.class)
    private BigDecimal  total;
}
