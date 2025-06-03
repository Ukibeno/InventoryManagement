package InventoryManagement.supplier;



import InventoryManagement.dto.SupplierDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.dto.UserDto;

import java.util.List;

public interface SupplierService {
    List<SupplierDto> getAllSuppliers();
    SupplierDto getSupplierById(Long id);

    SupplierDto updateSupplier(Long id, SupplierSignupRequestDto supplierSignupRequestDto);

    void approveSupplier(Long id);
    void rejectSupplier(Long id);

    void deleteSupplier(Long supplierId);

    }
