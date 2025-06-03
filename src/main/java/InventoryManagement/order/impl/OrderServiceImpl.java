package InventoryManagement.order.impl;

import InventoryManagement.dto.*;
import InventoryManagement.exception.ResourceNotFoundException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductRepository productRepository;

    @Override
    public OrderDto createOrder(OrderCreationRequestDto dto, User user) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .category(category)
                .status(Status.PENDING)
                .reviewMessage("Awaiting Admin review")
                .build();

        List<OrderItem> items = dto.getItems().stream()
                .map(itemDto -> {
                    OrderItem item = orderItemMapper.toEntity(itemDto);

                    Product product = productRepository.findByProductCode(itemDto.getProductCode())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with code " + itemDto.getProductCode()));
                    item.setProduct(product);
                    item.setOrder(order);
                    return item;
                }).collect(Collectors.toList());
        order.setItems(items);

        // Calculate total price
        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    public List<OrderDto> getAllOrdersForAdmin() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderManagerViewDto> getAllOrdersForManager(User user) {
        return orderRepository.findByUser(user).stream()
                .map(order -> {
                    OrderManagerViewDto dto = new OrderManagerViewDto();
                    dto.setId(order.getId());
                    dto.setOrderNumber(order.getOrderNumber());
                    dto.setCategory(new CategoryDto(order.getCategory().getId(), order.getCategory().getName(), order.getCategory().getDescription()));
                    dto.setItems(order.getItems().stream().map(orderItemMapper::toDto).collect(Collectors.toList()));
                    dto.setStatus(order.getStatus());
                    dto.setReviewMessage(order.getReviewMessage());
                    dto.setTotal(order.getTotal());
                    return dto;
                }).collect(Collectors.toList());
    }


    @Override
    public OrderDto reviewOrder(Long orderId, OrderReviewRequestDto reviewRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only pending orders can be reviewed");
        }

        order.setStatus(reviewRequest.isApproved() ? Status.APPROVED : Status.REJECTED);
        order.setReviewMessage(reviewRequest.getReviewMessage());

        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    public OrderDto assignSupplier(Long orderId, OrderAssignSupplierRequestDto assignSupplierRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != Status.APPROVED) {
            throw new IllegalStateException("Supplier can only be assigned to approved orders");
        }

        Supplier supplier = supplierRepository.findById(assignSupplierRequest.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        if (!supplier.getCategory().equals(order.getCategory())) {
            throw new IllegalStateException("Supplier category does not match order category");
        }

        order.setSupplier(supplier);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }


    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }
}


