package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 
    
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            
            return ResponseEntity.badRequest().build();
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Return the registered user
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        return userRepository.findByEmail(user.getEmail())
                .map(u -> {
                    u.setPassword(null); // Set the password to null before sending it back
                    return ResponseEntity.ok(u);
                })
                .orElse(ResponseEntity.badRequest().build());
    }

   

    
}


