package InventoryManagement.mapper;

import InventoryManagement.dto.OrderItemDto;
import InventoryManagement.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    OrderItem toEntity(OrderItemDto orderItemDto);
}
