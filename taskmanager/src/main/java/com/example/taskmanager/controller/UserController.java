package com.example.taskmanager.controller;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 注册
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if(userRepository.existsByUsername(user.getUsername()))
            return ResponseEntity.ok(Map.of("success", false, "message", "Username already exists"));
        if(userRepository.existsByEmail(user.getEmail()))
            return ResponseEntity.ok(Map.of("success", false, "message", "Email already exists"));

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("success", true, "message", "User registered successfully"));
    }

    // 登录
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String,String> loginData){
        String usernameOrEmail = loginData.get("usernameOrEmail");
        String password = loginData.get("password");

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if(user == null || !user.getPassword().equals(password))
            return ResponseEntity.ok(Map.of("success", false, "message", "Invalid credentials"));

        return ResponseEntity.ok(Map.of("success", true, "message", "Login successful", "user", user));
    }
}
