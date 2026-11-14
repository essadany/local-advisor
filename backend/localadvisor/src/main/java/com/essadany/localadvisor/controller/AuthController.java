package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.config.JwtUtil;
import com.essadany.localadvisor.dto.AuthResponse;
import com.essadany.localadvisor.dto.LoginRequest;
import com.essadany.localadvisor.dto.RegisterRequest;
import com.essadany.localadvisor.exception.ConflictException;
import com.essadany.localadvisor.model.Role;
import com.essadany.localadvisor.model.User;
import com.essadany.localadvisor.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.getUserByEmail(request.email()).isPresent()) {
            throw new ConflictException("Un utilisateur avec cet email existe déjà");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuthResponse.bearer(jwtUtil.generateToken(user.getEmail())));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        return ResponseEntity.ok(AuthResponse.bearer(jwtUtil.generateToken(authentication.getName())));
    }
}
