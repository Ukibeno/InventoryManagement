package InventoryManagement.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreationRequestDto {
    private CategoryDto categoryDto;
    private String name;
    private String description;
    private Integer quantity;
    private BigDecimal price;
}
