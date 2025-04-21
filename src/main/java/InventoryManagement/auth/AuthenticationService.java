package InventoryManagement.auth;

import InventoryManagement.config.CustomUserDetails;
import InventoryManagement.config.JwtService;
import InventoryManagement.dto.AdminSignupRequestDto;
import InventoryManagement.dto.SupplierSignupRequestDto;
import InventoryManagement.exception.EmailAlreadyExistsException;
import InventoryManagement.exception.ResourceNotFoundException;
import InventoryManagement.mapper.SupplierMapper;
import InventoryManagement.mapper.UserMapper;
import InventoryManagement.model.Role;
import InventoryManagement.model.Status;
import InventoryManagement.model.Supplier;
import InventoryManagement.model.User;
import InventoryManagement.repository.category.CategoryRepository;
import InventoryManagement.repository.supplier.SupplierRepository;
import InventoryManagement.repository.user.UserRepository;
import InventoryManagement.token.Token;
import InventoryManagement.token.TokenRepository;
import InventoryManagement.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final SupplierMapper supplierMapper;

    public AuthenticationResponse registerAdmin(AdminSignupRequestDto request) {
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(Status.ACTIVE);

        if (user.getRole() == null) {
            user.setRole(Role.ADMIN);
        }

        var savedUser = userRepository.save(user);
        return generateAuthenticationResponse(savedUser);
    }

    @Transactional
    public AuthenticationResponse registerSupplier(SupplierSignupRequestDto request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        // Map and prepare Supplier directly
        var supplier = (Supplier) supplierMapper.toEntity(request);
        supplier.setPassword(passwordEncoder.encode(request.getPassword()));
        supplier.setRole(Role.SUPPLIER);
        supplier.setStatus(Status.PENDING);

        if (request.getCategoryCreationRequestDto() != null) {
            var category = categoryRepository.findById(request.getCategoryCreationRequestDto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryCreationRequestDto().getId()));
            supplier.setCategory(category);
        }

        var savedSupplier = supplierRepository.save(supplier);

        return generateAuthenticationResponse(savedSupplier);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        revokeAllUserTokens(user);
        return generateAuthenticationResponse(user);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDetails userDetails = new CustomUserDetails(user);

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private AuthenticationResponse generateAuthenticationResponse(User user) {
        UserDetails userDetails = new CustomUserDetails(user);
        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);

        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
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
}
