package InventoryManagement.mapper;

import InventoryManagement.dto.OrderDto;
import InventoryManagement.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Mapping OrderDto to Order Entity
    Order toEntity(OrderDto dto);

    // Mapping Order Entity to OrderDto
    OrderDto toDto(Order order);

    List<OrderDto> toDto(List<Order> orders);

}

