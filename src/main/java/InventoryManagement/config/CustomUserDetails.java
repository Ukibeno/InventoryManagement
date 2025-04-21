package InventoryManagement.config;


import InventoryManagement.model.Status;
import InventoryManagement.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;


public class CustomUserDetails implements UserDetails {

    private final User user;

    public Long getId() {
        return user.getId();
    }

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Delegates to your User model to provide authorities
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Use email as username
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // Implement the remaining methods as appropriate
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == Status.APPROVED;  // Or whatever status you're using to mark an active account
    }

    // Optionally add access to the underlying User entity if needed
    public User getUser() {
        return user;
    }
}
