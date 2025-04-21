package InventoryManagement.supplier;

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

    // Admins and suppliers can view their own profile
    @PreAuthorize("hasAnyAuthority('admin:read', 'supplier:read')")
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable Long id) {
        SupplierDto supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    // Supplier can update their own profile
    @PreAuthorize("hasAuthority('supplier:update')")
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> updateSupplier(@PathVariable Long id,
                                                      @Valid @RequestBody SupplierSignupRequestDto supplierSignupRequestDto) {
        SupplierDto updatedSupplier = supplierService.updateSupplier(id, supplierSignupRequestDto);
        return ResponseEntity.ok(updatedSupplier);
    }

    // Admin only: list all suppliers
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        List<SupplierDto> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    // Admin only: approve supplier
    @PreAuthorize("hasAuthority('admin:update')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveSupplier(@PathVariable Long id) {
        supplierService.approveSupplier(id);
        return ResponseEntity.ok("Supplier approved successfully.");
    }

    // Admin only: reject supplier
    @PreAuthorize("hasAuthority('admin:update')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectSupplier(@PathVariable Long id) {
        supplierService.rejectSupplier(id);
        return ResponseEntity.ok("Supplier rejected successfully.");
    }

    // Admin only: delete supplier
    @PreAuthorize("hasAuthority('admin:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
