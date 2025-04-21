package InventoryManagement.supplier;



import InventoryManagement.dto.SupplierDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.dto.UserDto;

import java.util.List;

public interface SupplierService {
    List<SupplierDto> getAllSuppliers();
    SupplierDto getSupplierById(Long id);

   // SupplierDto createSupplier(SupplierSignupRequestDto supplierSignupRequestDto);

    SupplierDto updateSupplier(Long id, SupplierDto supplierDto);

    void approveSupplier(Long supplierId);

    void deleteSupplier(Long supplierId);

    }
