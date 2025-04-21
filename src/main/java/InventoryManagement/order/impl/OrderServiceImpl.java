package InventoryManagement.order.impl;

import InventoryManagement.dto.CategoryDto;
import InventoryManagement.dto.OrderDto;
import InventoryManagement.mapper.OrderItemMapper;
import InventoryManagement.mapper.OrderMapper;
import InventoryManagement.model.*;
import InventoryManagement.order.OrderService;
import InventoryManagement.repository.category.CategoryRepository;
import InventoryManagement.repository.order.OrderRepository;
import InventoryManagement.repository.product.ProductRepository;
import InventoryManagement.repository.supplier.SupplierRepository;
import InventoryManagement.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final OrderMapper orderMapper;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;

    @Override
    public OrderDto createOrder(CategoryDto categoryDto, OrderDto dto, User user) {
        // Null check for the user
        if (user == null) {
            throw new IllegalArgumentException("Authenticated user is required.");
        }

        // Ensure the user exists in the system before proceeding
        User users = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create the Order entity
        Order order = orderMapper.toEntity(dto);

        // Retrieve the category and associate it with the order
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        order.setCategory(category);

        // Set the order status to PENDING by default
        order.setStatus(Status.PENDING);

        // Set the createdBy user
        order.setCreatedBy(users);

        // Map OrderItems from DTO to entities using OrderItemMapper
        List<OrderItem> orderItems = dto.getOrderItems().stream()
                .map(orderItemMapper::toEntity)
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);

        // Save the order and return the DTO of the created order
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }


    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto approveOrRejectOrder(Long orderId, boolean isApproved) {
        // Ensure the order exists
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check if the order is in PENDING status
        if (order.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only pending orders can be approved or rejected.");
        }

        // Handle rejection logic
        if (!isApproved) {
            order.setStatus(Status.REJECTED);
            Order savedOrder = orderRepository.save(order);
            return orderMapper.toDto(savedOrder);
        }

        // Handle approval logic
        order.setStatus(Status.APPROVED);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto assignSupplierToOrder(Long orderId, Long supplierId) {
        // Ensure the order exists
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check if the order is in APPROVED status before assigning a supplier
        if (order.getStatus() != Status.APPROVED) {
            throw new IllegalStateException("Only approved orders can be assigned to a supplier.");
        }

        // Ensure the supplier exists and belongs to the correct category
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (!supplier.getCategory().equals(order.getCategory())) {
            throw new IllegalStateException("Supplier does not belong to the correct category.");
        }

        // Assign the supplier to the order
        order.setSupplier(supplier);

        // Save the updated order with the assigned supplier
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }
}
