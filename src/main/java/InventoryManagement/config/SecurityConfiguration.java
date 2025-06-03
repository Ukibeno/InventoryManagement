package InventoryManagement.config;

import InventoryManagement.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static InventoryManagement.model.Permission.*;
import static InventoryManagement.model.Role.*;
import static org.springframework.http.HttpMethod.*;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userRepository, passwordEncoder, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll()

                        // Categories
                        .requestMatchers("/api/v1/categories/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                        .requestMatchers(GET, "/api/v1/categories/**").hasAnyAuthority(ADMIN_READ.getPermission(), MANAGER_READ.getPermission())
                        .requestMatchers(POST, "/api/v1/categories/**").hasAuthority(ADMIN_CREATE.getPermission())
                        .requestMatchers(PUT, "/api/v1/categories/**").hasAuthority(ADMIN_UPDATE.getPermission())
                        .requestMatchers(DELETE, "/api/v1/categories/**").hasAuthority(ADMIN_DELETE.getPermission())

                        // Orders
                        .requestMatchers("/api/v1/orders/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                        .requestMatchers(GET, "/api/v1/orders/**").hasAuthority(ADMIN_READ.getPermission())
                        .requestMatchers(POST, "/api/v1/orders/**").hasAuthority(ADMIN_CREATE.getPermission())
                        // Note: Manager allowed to update orders, so add MANAGER_UPDATE permission if exists or allow by role
                        .requestMatchers(PUT, "/api/v1/orders/**").hasAnyAuthority(ADMIN_UPDATE.getPermission(), MANAGER_UPDATE.getPermission())
                        .requestMatchers(DELETE, "/api/v1/orders/**").hasAuthority(ADMIN_DELETE.getPermission())

                        // Suppliers
                        .requestMatchers("/api/v1/suppliers/**").hasAnyRole(ADMIN.name(), SUPPLIER.name())
                        .requestMatchers(GET, "/api/v1/suppliers/**").hasAuthority(ADMIN_READ.getPermission())
                        .requestMatchers(POST, "/api/v1/suppliers/**").hasAuthority(ADMIN_CREATE.getPermission())
                        .requestMatchers(PUT, "/api/v1/suppliers/**").hasAuthority(ADMIN_UPDATE.getPermission())
                        .requestMatchers(DELETE, "/api/v1/suppliers/**").hasAuthority(ADMIN_DELETE.getPermission())

                        // Products
                        .requestMatchers("/api/v1/products/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                        .requestMatchers(GET, "/api/v1/products/**").hasAuthority(ADMIN_READ.getPermission())
                        .requestMatchers(POST, "/api/v1/products/**").hasAuthority(ADMIN_CREATE.getPermission())
                        .requestMatchers(PUT, "/api/v1/products/**").hasAuthority(ADMIN_UPDATE.getPermission())
                        .requestMatchers(DELETE, "/api/v1/products/**").hasAuthority(ADMIN_DELETE.getPermission())

                        // Admin endpoints
                        .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
                        .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.getPermission())
                        .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.getPermission())
                        .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.getPermission())
                        .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.getPermission())

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
