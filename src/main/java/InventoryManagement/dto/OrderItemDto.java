package InventoryManagement.dto;

import InventoryManagement.dto.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    @JsonView({Views.ManagerView.class, Views.AdminView.class})
    private  String productCode;

    @JsonView({Views.ManagerView.class, Views.AdminView.class})
    private String productName;

    @JsonView({Views.ManagerView.class, Views.AdminView.class})
    private int quantity;

    @JsonView({Views.ManagerView.class, Views.AdminView.class})
    private BigDecimal price;
}
