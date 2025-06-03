
package InventoryManagement.dto;


import InventoryManagement.model.Role;
import InventoryManagement.model.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private Role role;
  private CategoryDto category;
  private Status status;
}

