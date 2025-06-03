package InventoryManagement.auth;

import InventoryManagement.dto.ApiSuccessResponse;
import InventoryManagement.dto.SupplierSignupRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/signup")
    public ResponseEntity<ApiSuccessResponse<AuthenticationResponse>> register(
          @Valid @RequestBody SupplierSignupRequestDto request
    ) {
        var authResponse = service.registerSupplier(request);
        var response = new ApiSuccessResponse<>(
                true,
                "Supplier registered successfully",
                authResponse,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiSuccessResponse<AuthenticationResponse>> authenticate(
         @Valid  @RequestBody AuthenticationRequest request
    ) {
        var authResponse = service.authenticate(request);
        var response = new ApiSuccessResponse<>(
                true,
                "Authentication successful",
                authResponse,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
}
