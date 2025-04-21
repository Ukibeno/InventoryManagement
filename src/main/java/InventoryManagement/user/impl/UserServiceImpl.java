package InventoryManagement.user.impl;

import InventoryManagement.auth.AuthenticationResponse;
import InventoryManagement.auth.AuthenticationService;
import InventoryManagement.config.CustomUserDetails;
import InventoryManagement.config.JwtService;
import InventoryManagement.dto.AdminSignupRequestDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.mapper.UserMapper;
import InventoryManagement.model.Role;
import InventoryManagement.model.User;
import InventoryManagement.repository.user.UserRepository;
import InventoryManagement.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;


    // Get all users
    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
    // Get user by ID
    @Override
    public UserDto getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found for id: " + id));
        return userMapper.toDto(user);
    }

    // Update user details
    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found for id: " + id));
        updateUserFields(user, userDto);
        User updatedUser = repository.save(user);
        return userMapper.toDto(updatedUser);
    }

    // Delete user
    @Override
    public void deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found for id: " + id));
        repository.delete(user);
    }

    // Helper to update user fields
    private void updateUserFields(User user, UserDto userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setContact(userDto.getContact());
    }
}
