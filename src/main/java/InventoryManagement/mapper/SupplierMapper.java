package InventoryManagement.mapper;

import InventoryManagement.dto.SupplierDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.model.Supplier;
import InventoryManagement.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface SupplierMapper {

    @Mapping(target = "status", source = "status")
    SupplierDto toDto(Supplier supplier);

    // When converting signup DTO to entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "SUPPLIER")
    @Mapping(target = "category", ignore = true) // will be set manually in service
    Supplier toEntity(SupplierSignupRequestDto dto);
}
