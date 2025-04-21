package InventoryManagement.repository.order;


import InventoryManagement.model.Order;
import InventoryManagement.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find order by ID
    Optional<Order> findById(Long id);

    // Find orders by the manager (i.e., orders created by a specific manager)
    //List<Order> findByManager(User user);

    // Find orders by status (e.g., PENDING, APPROVED, etc.)
    List<Order> findByStatus(Status status);

    // You can add other custom queries if needed (e.g., by category)
    List<Order> findByCategory_Id(Long categoryId);
}
