package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.config.JwtUtil;
import com.essadany.localadvisor.exception.ConflictException;
import com.essadany.localadvisor.model.User;
import com.essadany.localadvisor.repository.UserRepository;
import com.essadany.localadvisor.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            throw new ConflictException("L'utilisateur existe déja");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(com.essadany.localadvisor.model.Role.USER);
        }
        userService.saveUser(user);
        HttpStatus created = HttpStatus.CREATED;
        response.put("status", created.value());
        response.put("message", "Utilisateur enregistré avec succès");
        return ResponseEntity.status(created).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        Map<String, Object> authData = new HashMap<>();
        authData.put("token", jwtUtil.generateToken(user.getEmail()));
        authData.put("type", "Bearer");
        return ResponseEntity.ok(authData);
    }

}

