package InventoryManagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private CategoryDto category;
    private String name;
    private String description;
    private Integer quantity;
    private BigDecimal price;

}
