package InventoryManagement.mapper;

import InventoryManagement.dto.SupplierDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.model.Supplier;
import InventoryManagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = SupplierMapper.class)
public interface SupplierMapper {
    @Mappings({
            @Mapping(target = "categoryDto", source = "category"), // map category entity to categoryDto
            @Mapping(target = "status", source = "status")         // map status field directly
    })
    SupplierDto toDto(Supplier supplier);

    @Mappings({
            @Mapping(target = "role", constant = "SUPPLIER"),
            @Mapping(target = "status", constant = "PENDING")
    })
    Supplier toEntity(SupplierSignupRequestDto request);
}
