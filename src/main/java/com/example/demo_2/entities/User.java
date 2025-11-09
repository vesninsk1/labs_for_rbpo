package com.example.demo_2.entities;

import com.example.demo_2.models.ApplicationUserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "credit_card")
    private String creditCard;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private ApplicationUserRole role = ApplicationUserRole.USER;

    @Builder.Default
    @Column(name = "account_expired")
    private boolean isAccountExpired = false;

    @Builder.Default
    @Column(name = "account_locked")
    private boolean isAccountLocked = false;

    @Builder.Default
    @Column(name = "credentials_expired")
    private boolean isCredentialsExpired = false;

    @Builder.Default
    @Column(name = "disabled")
    private boolean isDisabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getGrantedAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return !isDisabled;
    }
}