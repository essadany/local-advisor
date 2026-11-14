package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.model.Favorite;
import com.essadany.localadvisor.service.UserService;
import com.essadany.localadvisor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
class UserController {
    @Autowired
    private UserService UserService;


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        if (UserService.getAllUsers().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(UserService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User userToUpdate = UserService.getUserById(id);
        if (userToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        return ResponseEntity.ok(UserService.updateUser(user));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        UserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = UserService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

}