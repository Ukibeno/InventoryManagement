package InventoryManagement.user;


import InventoryManagement.auth.AuthenticationRequest;
import InventoryManagement.auth.AuthenticationResponse;
import InventoryManagement.dto.AdminSignupRequestDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.model.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

}
