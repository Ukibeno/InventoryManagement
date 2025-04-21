package InventoryManagement.order.impl;

import InventoryManagement.dto.OrderCreationRequestDto;
import InventoryManagement.dto.OrderDto;
import InventoryManagement.exception.ResourceNotFoundException;
import InventoryManagement.mapper.OrderItemMapper;
import InventoryManagement.mapper.OrderMapper;
import InventoryManagement.model.*;
import InventoryManagement.order.OrderService;
import InventoryManagement.repository.category.CategoryRepository;
import InventoryManagement.repository.order.OrderRepository;
import InventoryManagement.repository.supplier.SupplierRepository;
import InventoryManagement.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto createOrder(OrderCreationRequestDto orderCreationRequestDto, User user) {
        // 1) Get authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        // 2) Fetch category by name
        String categoryName = orderCreationRequestDto.getCategoryDto().getName();
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryName));

        // 3) Set order status based on role
        Status initialStatus = creator.getRole() == Role.ADMIN
                ? Status.APPROVED
                : Status.PENDING;

        // 4) Build Order
        Order order = Order.builder()
                .orderNumber(orderCreationRequestDto.getOrderNumber())
                .user(creator)
                .status(initialStatus)
                .category(category)
                .build();

        // 5) Map OrderItems and set back-reference
        List<OrderItem> items = orderCreationRequestDto.getItems().stream()
                .map(itemDto -> {
                    OrderItem item = orderItemMapper.toEntity(itemDto);
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toList());
        order.setItems(items);

        // 6) Persist and return
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto approveOrRejectOrder(Long orderId, boolean isApproved) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only pending orders can be approved or rejected.");
        }

        order.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    public OrderDto assignSupplierToOrder(Long orderId, Long supplierId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() != Status.APPROVED) {
            throw new IllegalStateException("Only approved orders can be assigned to a supplier.");
        }

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + supplierId));

        if (!supplier.getCategory().equals(order.getCategory())) {
            throw new IllegalStateException("Supplier does not belong to the correct category.");
        }

        order.setSupplier(supplier);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }
}
