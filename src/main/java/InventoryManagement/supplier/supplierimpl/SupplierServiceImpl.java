package InventoryManagement.supplier.supplierimpl;

import InventoryManagement.dto.SupplierDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.exception.ResourceNotFoundException;
import InventoryManagement.mapper.SupplierMapper;
import InventoryManagement.model.Category;
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
    public SupplierDto getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found for id: " + supplierId));
        return supplierMapper.toDto(supplier);
    }

    @Override
    public SupplierDto updateSupplier(Long supplierId, SupplierSignupRequestDto dto) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found for id: " + supplierId));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        supplier.setFirstName(dto.getFirstName());
        supplier.setLastName(dto.getLastName());
        supplier.setEmail(dto.getEmail());
        supplier.setContact(dto.getContact());
        supplier.setAddress(dto.getAddress());
        supplier.setCategory(category);

        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public void approveSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found for id: " + supplierId));
        supplier.setStatus(Status.ACTIVE);
        supplierRepository.save(supplier);
    }

    @Override
    public void rejectSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found for id: " + supplierId));
        supplier.setStatus(Status.REJECTED);
        supplierRepository.save(supplier);
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        if (!supplierRepository.existsById(supplierId)) {
            throw new ResourceNotFoundException("Supplier not found for id: " + supplierId);
        }
        supplierRepository.deleteById(supplierId);
    }
}
