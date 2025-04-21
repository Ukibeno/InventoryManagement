package InventoryManagement.auth;

import InventoryManagement.config.CustomUserDetails;
import InventoryManagement.config.JwtService;
import InventoryManagement.dto.AdminSignupRequestDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.dto.UserDto;
import InventoryManagement.mapper.SupplierMapper;
import InventoryManagement.mapper.UserMapper;
import InventoryManagement.model.Role;
import InventoryManagement.model.User;
import InventoryManagement.repository.category.CategoryRepository;
import InventoryManagement.repository.user.UserRepository;
import InventoryManagement.token.Token;
import InventoryManagement.token.TokenRepository;
import InventoryManagement.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final SupplierMapper supplierMapper;
    private final CategoryRepository categoryRepository;

    public AuthenticationResponse registerAdmin(AdminSignupRequestDto request) {
        // Use the mapper instead of manual build
        var user = userMapper.toEntity(request);

        // encode password manually after mapping
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // if role is missing, you can fallback to ADMIN
        if (user.getRole() == null) {
            user.setRole(Role.ADMIN);
        }

        var savedUser = repository.save(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(savedUser);

        var jwtToken = jwtService.generateToken(customUserDetails);
        var refreshToken = jwtService.generateRefreshToken(customUserDetails);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse registerSupplier(SupplierSignupRequestDto request) {
        // 1. Map request to User
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.SUPPLIER);

        var savedUser = repository.save(user);

        // 2. Create a Supplier linked to User
        var supplier = supplierMapper.toEntity(request);
        supplier.setUser(savedUser); // link the User you just saved

        if (request.getCategoryDto() != null) {
            supplier.setCategory(categoryRepository.findById(request.getCategoryDto().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found for id: " + request.getCategoryDto().getId())));
        }

        // Save the Supplier separately
        supplierRepository.save(supplier);

        // 3. Create JWT Tokens
        CustomUserDetails customUserDetails = new CustomUserDetails(savedUser);
        var jwtToken = jwtService.generateToken(customUserDetails);
        var refreshToken = jwtService.generateRefreshToken(customUserDetails);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails userDetails = new CustomUserDetails(user);

        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = repository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDetails userDetails = new CustomUserDetails(user);

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                var authResponse = new AuthenticationResponse(accessToken, refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
