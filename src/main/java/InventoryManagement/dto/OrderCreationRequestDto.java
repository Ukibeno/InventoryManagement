package InventoryManagement.dto;

import InventoryManagement.dto.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequestDto {
    @NotNull(message = "Category is required!")
    @JsonView(Views.ManagerView.class)
    private Long categoryId;

    @NotNull(message = "Items is required!")
    @NotEmpty(message = "Items list cannot be empty!")
    @JsonView(Views.ManagerView.class)
    private List<OrderItemDto> items;
}
