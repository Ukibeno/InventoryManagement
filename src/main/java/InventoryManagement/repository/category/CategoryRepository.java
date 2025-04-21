package InventoryManagement.repository.category;

import java.util.Optional;


import InventoryManagement.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Find category by ID
    Optional<Category> findById(Long id);

    // You can add other custom queries if needed, such as by name
    Optional<Category> findByName(String name);
}

