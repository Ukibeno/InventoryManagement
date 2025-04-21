package InventoryManagement.mapper;

import InventoryManagement.dto.AdminSignupRequestDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Mapping from User to UserDto, where Supplier's category is mapped to categoryDto
    UserDto toDto(User user);

    User toEntity(AdminSignupRequestDto dto);
}
