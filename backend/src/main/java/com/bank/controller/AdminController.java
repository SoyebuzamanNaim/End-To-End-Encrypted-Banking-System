package com.bank.controller;

import com.bank.entity.User;
import com.bank.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Explicitly require the ADMIN role. In production, this requires the JWT to have roles mapped securely.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<Map<String, String>> getAllUsers() {
        // Because of the Application-Level Encryption (CryptoConverter), the backend holds the key
        // and decrypts the email and fullName automatically when fetching from DB.
        // We will return the decrypted data to the application admin.
        List<User> users = userRepository.findAll();
        
        return users.stream().map(user -> Map.of(
                "id", user.getId().toString(),
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "role", user.getRole()
        )).collect(Collectors.toList());
    }
}
