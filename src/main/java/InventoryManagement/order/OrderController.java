package InventoryManagement.order;

import InventoryManagement.dto.OrderCreationRequestDto;
import InventoryManagement.dto.OrderDto;
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
     * Admin orders are auto-approved, others are set to PENDING.
     */
    @PreAuthorize("hasAnyAuthority('admin:create', 'management:create')")
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(
            @Valid @RequestBody OrderCreationRequestDto request,
            @AuthenticationPrincipal User user
    ) {
        OrderDto created = orderService.createOrder(request, user);
        return ResponseEntity.ok(created);
    }

    /**
     * Fetch all orders.
     * Restricted to admins only.
     */
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Approve or reject a pending order.
     * Restricted to admins only.
     */
    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping("/{orderId}/approve")
    public ResponseEntity<OrderDto> approveOrRejectOrder(
            @PathVariable Long orderId,
            @RequestParam boolean isApproved
    ) {
        return ResponseEntity.ok(orderService.approveOrRejectOrder(orderId, isApproved));
    }

    /**
     * Assign a supplier to an approved order.
     * Restricted to admins only.
     */
    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping("/{orderId}/assign-supplier/{supplierId}")
    public ResponseEntity<OrderDto> assignSupplierToOrder(
            @PathVariable Long orderId,
            @PathVariable Long supplierId
    ) {
        return ResponseEntity.ok(orderService.assignSupplierToOrder(orderId, supplierId));
    }
}
