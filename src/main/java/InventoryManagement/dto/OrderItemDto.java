package InventoryManagement.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private  String productCode;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
