package InventoryManagement.user;

import InventoryManagement.auth.AuthenticationResponse;
import InventoryManagement.auth.AuthenticationService;
import InventoryManagement.dto.ApiSuccessResponse;
import InventoryManagement.dto.UserSignupRequestDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.model.Status;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN_CREATE')")
    @PostMapping("/register-user")
    public ResponseEntity<ApiSuccessResponse<AuthenticationResponse>> register(
           @Valid @RequestBody UserSignupRequestDto request
    ) {
        AuthenticationResponse authResponse = authenticationService.registerAdmin(request);
        ApiSuccessResponse<AuthenticationResponse> response = new ApiSuccessResponse<>(
                true,
                "Admin registered successfully",
                authResponse,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @GetMapping("")
    public ResponseEntity<ApiSuccessResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        ApiSuccessResponse<List<UserDto>> response = new ApiSuccessResponse<>(
                true,
                "Users fetched successfully",
                users,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<UserDto>> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        ApiSuccessResponse<UserDto> response = new ApiSuccessResponse<>(
                true,
                "User fetched successfully",
                user,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<UserDto>> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        ApiSuccessResponse<UserDto> response = new ApiSuccessResponse<>(
                true,
                "User updated successfully",
                updatedUser,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiSuccessResponse<Void> response = new ApiSuccessResponse<>(
                true,
                "User deleted successfully",
                null,
                HttpStatus.NO_CONTENT.value()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}
