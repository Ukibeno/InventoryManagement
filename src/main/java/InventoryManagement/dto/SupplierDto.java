
package InventoryManagement.dto;


import InventoryManagement.model.BaseEntity;
import InventoryManagement.model.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDto {
  private String firstName;
  private String lastName;
  private String email;
  private Role role;
  private CategoryDto categoryDto;
}

