package InventoryManagement.supplier.supplierimpl;

import InventoryManagement.dto.SupplierDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.mapper.SupplierMapper;
import InventoryManagement.model.Category;
import InventoryManagement.model.Role;
import InventoryManagement.model.Status;
import InventoryManagement.model.Supplier;
import InventoryManagement.repository.category.CategoryRepository;
import InventoryManagement.repository.supplier.SupplierRepository;
import InventoryManagement.supplier.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierMapper supplierMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toDto)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public SupplierDto getSupplierById(Long id) {
        return supplierMapper.toDto(findSupplierById(id));
    }

    @Override
    public SupplierDto updateSupplier(Long id, SupplierSignupRequestDto dto) {
        Supplier supplier = findSupplierById(id);
        Category category = findCategoryById(dto.getCategoryCreationRequestDto().getId());

        supplier.setFirstName(dto.getFirstName());
        supplier.setLastName(dto.getLastName());
        supplier.setEmail(dto.getEmail());
        supplier.setCategory(category);

        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    public void approveSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        supplier.setStatus(Status.ACTIVE);
        supplierRepository.save(supplier);
    }

    public void rejectSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        supplier.setStatus(Status.REJECTED);
        supplierRepository.save(supplier);
    }
    @Override
    public void deleteSupplier(Long supplierId) {
        Supplier supplier = findSupplierById(supplierId);
        supplierRepository.delete(supplier);
    }

    private Supplier findSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found for id: " + id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found for id: " + id));
    }
}
