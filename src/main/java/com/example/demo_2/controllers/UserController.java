package com.example.demo_2.controllers;
import com.example.demo_2.entities.User;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    
    @PostMapping
    public User createUser(@RequestBody User user) {
        User newUser = User.builder()
            .id(idCounter.getAndIncrement())
            .name(user.getName())
            .email(user.getEmail())
            .credit_card(user.getCredit_card())
            .build();
        users.add(newUser);
        return newUser;
    }

    
    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }

    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return users.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        users.removeIf(user -> user.getId().equals(id));
        return "User deleted";
    }

    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        deleteUser(id);
        User updatedUser = User.builder()
            .id(id)
            .name(user.getName())
            .email(user.getEmail())
            .credit_card(user.getCredit_card())
            .build();
        users.add(updatedUser);
        return updatedUser;
    }
}
