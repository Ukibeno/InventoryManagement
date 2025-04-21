package InventoryManagement.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreationRequestDto {
    private Long id;
    private String name;
    private String description;
}
