package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.User;
import com.essadany.localadvisor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
class UserController {
    @Autowired
    private UserService UserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(UserService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User update) {
        User existing = UserService.getUserById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("User " + id + " not found");
        }
        if (update.getName() != null) {
            existing.setName(update.getName());
        }
        if (update.getEmail() != null) {
            existing.setEmail(update.getEmail());
        }
        if (update.getPassword() != null && !update.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(update.getPassword()));
        }
        return ResponseEntity.ok(UserService.updateUser(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (UserService.getUserById(id) == null) {
            throw new ResourceNotFoundException("User " + id + " not found");
        }
        UserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return UserService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
    }

}
