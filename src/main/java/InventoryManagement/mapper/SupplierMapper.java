package InventoryManagement.mapper;

import InventoryManagement.dto.SupplierDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.model.Supplier;
import InventoryManagement.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SupplierMapper.class)
public interface SupplierMapper {

    SupplierDto toDto(Supplier supplier);

    Supplier toEntity(SupplierSignupRequestDto dto);
}
