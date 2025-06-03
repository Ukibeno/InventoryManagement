package InventoryManagement.order;

import InventoryManagement.dto.*;
import InventoryManagement.model.User;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderCreationRequestDto dto, User user);
    List<OrderDto> getAllOrdersForAdmin();
    List<OrderManagerViewDto> getAllOrdersForManager(User manager);
    OrderDto reviewOrder(Long orderId, OrderReviewRequestDto reviewRequest);
    OrderDto assignSupplier(Long orderId, OrderAssignSupplierRequestDto assignSupplierRequest);
}

