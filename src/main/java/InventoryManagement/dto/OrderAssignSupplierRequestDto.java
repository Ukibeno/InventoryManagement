package InventoryManagement.dto;

import InventoryManagement.dto.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAssignSupplierRequestDto {
    @JsonView(Views.ManagerView.class)
    private Long supplierId;
}
