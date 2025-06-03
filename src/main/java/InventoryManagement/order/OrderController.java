package InventoryManagement.order;

import InventoryManagement.dto.*;
import InventoryManagement.dto.view.Views;
import InventoryManagement.model.User;
import InventoryManagement.order.OrderService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PreAuthorize("hasAnyAuthority('ADMIN_CREATE', 'MANAGEMENT_CREATE')")
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<OrderDto>> createOrder(
            @Valid @RequestBody OrderCreationRequestDto request,
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        OrderDto created = orderService.createOrder(request, user);
        ApiSuccessResponse<OrderDto> response = new ApiSuccessResponse<>(
                true,
                "Order created successfully",
                created,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @GetMapping
    @JsonView(Views.AdminView.class)
    public ResponseEntity<ApiSuccessResponse<List<OrderDto>>> getAllOrdersForAdmin() {
        List<OrderDto> orders = orderService.getAllOrdersForAdmin();
        ApiSuccessResponse<List<OrderDto>> response = new ApiSuccessResponse<>(
                true,
                "Orders fetched successfully",
                orders,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('MANAGEMENT_READ')")
    @GetMapping("/my-orders")
    @JsonView(Views.ManagerView.class)
    public ResponseEntity<ApiSuccessResponse<List<OrderManagerViewDto>>> getOrdersForManager(
            @AuthenticationPrincipal User user
    ) {
        List<OrderManagerViewDto> orders = orderService.getAllOrdersForManager(user);
        ApiSuccessResponse<List<OrderManagerViewDto>> response = new ApiSuccessResponse<>(
                true,
                "Manager orders fetched successfully",
                orders,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_UPDATE')")
    @PutMapping("/{orderId}/review")
    public ResponseEntity<ApiSuccessResponse<OrderDto>> reviewOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderReviewRequestDto reviewRequest
    ) {
        OrderDto updatedOrder = orderService.reviewOrder(orderId, reviewRequest);
        ApiSuccessResponse<OrderDto> response = new ApiSuccessResponse<>(
                true,
                "Order reviewed successfully",
                updatedOrder,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN_UPDATE')")
    @PutMapping("/{orderId}/assign-supplier")
    public ResponseEntity<ApiSuccessResponse<OrderDto>> assignSupplier(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderAssignSupplierRequestDto assignSupplierRequest
    ) {
        OrderDto updatedOrder = orderService.assignSupplier(orderId, assignSupplierRequest);
        ApiSuccessResponse<OrderDto> response = new ApiSuccessResponse<>(
                true,
                "Supplier assigned successfully",
                updatedOrder,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}
