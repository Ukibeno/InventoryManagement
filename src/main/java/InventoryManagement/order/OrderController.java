package InventoryManagement.order;

import InventoryManagement.dto.CategoryDto;
import InventoryManagement.dto.OrderCreationRequest;
import InventoryManagement.dto.OrderDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.model.User;
import InventoryManagement.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Create a new order.
     * Accessible by both ADMIN and MANAGER roles.
     *
     * @param request the composite request containing order number, category, and order data.
     * @param user    the authenticated user.
     * @return ResponseEntity containing the created OrderDto.
     */
    @PreAuthorize("hasAnyAuthority('admin:create', 'management:create')")
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(
           @Valid @RequestBody OrderCreationRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                orderService.createOrder(request.getCategory(), request.getOrder(), user)
        );
    }
    /**
     * Get orders by MANAGER.
     * Accessible by MANAGER role.
     *
     * @param user the authenticated user.
     * @return ResponseEntity containing the list of OrderDto.
     */
   /* @PreAuthorize("hasAuthority('management:read')")
    @GetMapping("/manager")
    public ResponseEntity<List<OrderDto>> getOrdersByManager(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getOrdersByManager(user));
    }*/

    /**
     * Get all orders.
     * Accessible by ADMIN role.
     *
     * @return ResponseEntity containing the list of OrderDto.
     */
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {

        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Approve or reject an order.
     * Accessible by ADMIN role.
     *
     * @param orderId the ID of the order to approve/reject.
     * @param isApproved whether the order is approved.
     * @return ResponseEntity containing the updated OrderDto.
     */
    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping("/{orderId}/approve")
    public ResponseEntity<OrderDto> approveOrRejectOrder(
            @PathVariable Long orderId,
            @RequestParam boolean isApproved) {
        return ResponseEntity.ok(orderService.approveOrRejectOrder(orderId, isApproved));
    }



    /**
     * Assign a supplier to an order.
     * Accessible by ADMIN role.
     *
     * @param orderId the ID of the order.
     * @param supplierId the ID of the supplier.
     * @return ResponseEntity containing the updated OrderDto.
     */
    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping("/{orderId}/assign-supplier/{supplierId}")
    public ResponseEntity<OrderDto> assignSupplierToOrder(
            @PathVariable Long orderId,
            @PathVariable Long supplierId) {
        return ResponseEntity.ok(orderService.assignSupplierToOrder(orderId, supplierId));
    }
}
