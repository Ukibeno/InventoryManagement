package InventoryManagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer id;
    private CategoryDto categoryDto;
    private String name;
    private String description;
    private Integer quantity;
    private BigDecimal price;

}
