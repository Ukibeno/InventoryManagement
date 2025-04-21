package InventoryManagement.mapper;

import InventoryManagement.dto.OrderCreationRequestDto;
import InventoryManagement.dto.OrderDto;
import InventoryManagement.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring", uses = { OrderItemMapper.class, UserMapper.class, CategoryMapper.class, SupplierMapper.class })
public interface OrderMapper {

    // Mapping OrderDto to Order Entity
    Order toEntity(OrderCreationRequestDto orderCreationRequestDto);

    // Mapping Order Entity to OrderDto
    OrderDto toDto(Order order);

    List<OrderDto> toDto(List<Order> orders);

}

