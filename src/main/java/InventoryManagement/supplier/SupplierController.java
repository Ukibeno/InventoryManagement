package InventoryManagement.supplier;

import InventoryManagement.dto.ApiSuccessResponse;
import InventoryManagement.dto.SupplierDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    // Admin and Supplier (for viewing profile)
    @PreAuthorize("hasAnyAuthority('ADMIN_READ', 'SUPPLIER_READ')")
    @GetMapping("/{supplierId}")
    public ResponseEntity<ApiSuccessResponse<SupplierDto>> getSupplierById(@PathVariable Long supplierId) {
        SupplierDto supplier = supplierService.getSupplierById(supplierId);
        ApiSuccessResponse<SupplierDto> response = new ApiSuccessResponse<>(
                true,
                "Supplier fetched successfully",
                supplier,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    // Supplier updates their own profile
    @PreAuthorize("hasAuthority('SUPPLIER_UPDATE')")
    @PutMapping("/{supplierId}")
    public ResponseEntity<ApiSuccessResponse<SupplierDto>> updateSupplier(
            @PathVariable Long supplierId,
            @Valid @RequestBody SupplierSignupRequestDto requestDto
    ) {
        SupplierDto updatedSupplier = supplierService.updateSupplier(supplierId, requestDto);
        ApiSuccessResponse<SupplierDto> response = new ApiSuccessResponse<>(
                true,
                "Supplier updated successfully",
                updatedSupplier,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<SupplierDto>>> getAllSuppliers() {
        List<SupplierDto> suppliers = supplierService.getAllSuppliers();
        ApiSuccessResponse<List<SupplierDto>> response = new ApiSuccessResponse<>(
                true,
                "Suppliers fetched successfully",
                suppliers,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    // Admin approves a supplier
    @PreAuthorize("hasAuthority('ADMIN_UPDATE')")
    @PutMapping("/{supplierId}/approve")
    public ResponseEntity<ApiSuccessResponse<String>> approveSupplier(@PathVariable Long supplierId) {
        supplierService.approveSupplier(supplierId);
        ApiSuccessResponse<String> response = new ApiSuccessResponse<>(
                true,
                "Supplier approved successfully",
                null,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    // Admin rejects a supplier
    @PreAuthorize("hasAuthority('ADMIN_UPDATE')")
    @PutMapping("/{supplierId}/reject")
    public ResponseEntity<ApiSuccessResponse<String>> rejectSupplier(@PathVariable Long supplierId) {
        supplierService.rejectSupplier(supplierId);
        ApiSuccessResponse<String> response = new ApiSuccessResponse<>(
                true,
                "Supplier rejected successfully",
                null,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    // Admin deletes a supplier
    @PreAuthorize("hasAuthority('ADMIN_DELETE')")
    @DeleteMapping("/{supplierId}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteSupplier(@PathVariable Long supplierId) {
        supplierService.deleteSupplier(supplierId);
        ApiSuccessResponse<Void> response = new ApiSuccessResponse<>(
                true,
                "Supplier deleted successfully",
                null,
                HttpStatus.NO_CONTENT.value()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
