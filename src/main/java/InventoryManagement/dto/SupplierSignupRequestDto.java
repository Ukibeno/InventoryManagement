package InventoryManagement.dto;

import InventoryManagement.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierSignupRequestDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String contact;
    @NotBlank
    private String address;
    @NotBlank
    private Status status;
    @NotBlank
    private CategoryCreationRequestDto categoryCreationRequestDto;
}
