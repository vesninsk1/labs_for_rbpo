package com.example.demo_2.services;

import com.example.demo_2.entities.User;
import com.example.demo_2.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        System.out.println("=== DEBUG UserDetailsServiceImpl ===");
        System.out.println("User: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
        System.out.println("Authorities: " + user.getAuthorities());
        System.out.println("===================================");
        
        return user; // Возвращаем саму сущность User
    }
}