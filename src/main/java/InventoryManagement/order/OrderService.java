package InventoryManagement.order;

import InventoryManagement.dto.OrderCreationRequestDto;
import InventoryManagement.dto.OrderDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.model.User;

import java.util.List;

public interface OrderService {

    /**
     * Create a new order.
     * - ADMIN → auto‑approved
     * - MANAGER → pending
     */
    OrderDto createOrder(OrderCreationRequestDto orderCreationRequestDto, User user);


    OrderDto approveOrRejectOrder(Long orderId, boolean isApproved);


    OrderDto assignSupplierToOrder(Long orderId, Long supplierId);

    /**
     * List all orders in the system.
     */
    List<OrderDto> getAllOrders();
}
