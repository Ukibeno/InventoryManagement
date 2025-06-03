package InventoryManagement.repository.order;


import InventoryManagement.model.Order;
import InventoryManagement.model.Status;
import InventoryManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find order by ID
    Optional<Order> findById(Long id);

    List<Order> findByStatus(Status status);

    // You can add other custom queries if needed (e.g., by category)
    List<Order> findByCategory_Id(Long categoryId);

    List<Order> findByUser(User user);
}
