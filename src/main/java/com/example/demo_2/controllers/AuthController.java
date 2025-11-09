// AuthController.java
package com.example.demo_2.controllers;

import com.example.demo_2.entities.User;
import com.example.demo_2.models.ApplicationUserRole;
import com.example.demo_2.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

    // Публичная регистрация - доступна без авторизации
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        try {
            // Валидация обязательных полей
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email обязателен для заполнения"));
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Пароль обязателен для заполнения"));
            }

            if (request.getCreditCard() == null || request.getCreditCard().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Номер кредитной карты обязателен для заполнения"));
            }

            // Валидация формата кредитной карты (базовая проверка)
            if (!isValidCreditCard(request.getCreditCard())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Неверный формат кредитной карты"));
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email уже существует"));
            }

            if (!isPasswordValid(request.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", 
                            "Длина пароля должна составлять не менее 8 символов, содержать как минимум одну цифру, " +
                            "одну строчную букву, одну заглавную букву, один специальный символ (@#$%^&+=!), " +
                            "и не содержать пробелов"));
            }

            // Создание пользователя с кредитной картой
            User user = User.builder()
                    .name(request.getName() != null ? request.getName() : request.getEmail().split("@")[0])
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .creditCard(request.getCreditCard())
                    .role(ApplicationUserRole.USER)
                    .build();

            User savedUser = userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "message", "Пользователь успешно зарегистрирован",
                    "email", savedUser.getEmail(),
                    "name", savedUser.getName(),
                    "role", savedUser.getRole().name(),
                    "id", savedUser.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка при регистрации: " + e.getMessage()));
        }
    }

    // Регистрация ADMIN - только для аутентифицированных ADMIN
    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody RegistrationRequest request) {
        try {
            // Валидация обязательных полей
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email обязателен для заполнения"));
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Пароль обязателен для заполнения"));
            }

            if (request.getCreditCard() == null || request.getCreditCard().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Номер кредитной карты обязателен для заполнения"));
            }

            // Валидация формата кредитной карты
            if (!isValidCreditCard(request.getCreditCard())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Неверный формат кредитной карты"));
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email уже существует"));
            }

            if (!isPasswordValid(request.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", 
                            "Длина пароля должна составлять не менее 8 символов, содержать как минимум одну цифру, " +
                            "одну строчную букву, одну заглавную букву, один специальный символ (@#$%^&+=!), " +
                            "и не содержать пробелов"));
            }

            User user = User.builder()
                    .name(request.getName() != null ? request.getName() : request.getEmail().split("@")[0])
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .creditCard(request.getCreditCard())
                    .role(ApplicationUserRole.ADMIN)
                    .build();

            User savedUser = userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "message", "Администратор успешно зарегистрирован",
                    "email", savedUser.getEmail(),
                    "name", savedUser.getName(),
                    "role", savedUser.getRole().name(),
                    "id", savedUser.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка при регистрации администратора: " + e.getMessage()));
        }
    }

    @GetMapping("/password-requirements")
    public ResponseEntity<Map<String, String>> getPasswordRequirements() {
        return ResponseEntity.ok(Map.of(
            "requirements", 
            "Длина пароля должна составлять не менее 8 символов, содержать как минимум одну цифру, " +
                        "одну строчную букву, одну заглавную букву, один специальный символ (@#$%^&+=!), " +
                        "и не содержать пробелов"
        ));
    }

    private boolean isPasswordValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private boolean isValidCreditCard(String creditCard) {
        // Базовая валидация кредитной карты - можно улучшить
        // Убираем пробелы и дефисы для проверки
        String cleanedCard = creditCard.replaceAll("[\\s-]+", "");
        return cleanedCard.matches("\\d{13,19}"); // От 13 до 19 цифр
    }

    public static class RegistrationRequest {
        private String name;
        private String email;
        private String password;
        private String creditCard;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getCreditCard() { return creditCard; }
        public void setCreditCard(String creditCard) { this.creditCard = creditCard; }
    }
}