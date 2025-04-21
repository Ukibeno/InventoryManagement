package InventoryManagement.mapper;

import InventoryManagement.dto.AdminSignupRequestDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "status", target = "status")
    @Mapping(source = "role", target = "role")
    UserDto toDto(User user);

    User toEntity(AdminSignupRequestDto dto);

    @Mapping(target = "role", constant = "SUPPLIER") // force role to SUPPLIER when mapping SupplierSignupRequestDto
    User toEntity(SupplierSignupRequestDto dto);
}
