package InventoryManagement.order;


import InventoryManagement.dto.CategoryDto;
import InventoryManagement.dto.OrderDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.model.User;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(CategoryDto categoryDto, OrderDto dto, User user);
    OrderDto approveOrRejectOrder(Long orderId, boolean isApproved);

    // OrderDto editOrder(Long orderId, OrderDto orderDto);

    OrderDto assignSupplierToOrder(Long orderId, Long supplierId);

    List<OrderDto> getAllOrders();

    //List<OrderDto> getOrdersByManager(User user);
}
