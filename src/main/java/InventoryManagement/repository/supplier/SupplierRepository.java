package InventoryManagement.repository.supplier;


import InventoryManagement.model.Supplier;
import InventoryManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}
