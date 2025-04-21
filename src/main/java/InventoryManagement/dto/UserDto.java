package InventoryManagement.dto;


import InventoryManagement.model.BaseEntity;
import InventoryManagement.model.Role;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto  {
    private String email;
    private String firstName;
    private String lastName;
    private String contact;
    private Role role;
}
