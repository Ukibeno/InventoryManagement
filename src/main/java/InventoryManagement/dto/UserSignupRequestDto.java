package InventoryManagement.dto;

import InventoryManagement.model.Role;
import InventoryManagement.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {
    @NotBlank(message="First Name is required")
    private String firstName;

    @NotBlank(message="last Name is required")
    private String lastName;

    @Email
    @NotBlank(message="Email is required")
    private String email;

    @NotBlank(message="password is required")
    private String password;

    @NotBlank(message = "contact is required")
    private String contact;

    @NotBlank(message="address is required")
    private String address;

    @NotBlank(message ="role is required!")
    private Role role;
}
