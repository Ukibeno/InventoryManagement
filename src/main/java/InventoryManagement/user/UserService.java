package InventoryManagement.user;


import InventoryManagement.dto.UserDto;
import InventoryManagement.model.Status;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

}
