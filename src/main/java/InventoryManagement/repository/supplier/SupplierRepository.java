package InventoryManagement.repository.supplier;


import InventoryManagement.model.Supplier;
import InventoryManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findById(Long id);


}
