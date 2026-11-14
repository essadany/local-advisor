package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.exception.ResourceNotFoundException;
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
        return ResponseEntity.ok(UserService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User userToUpdate = UserService.getUserById(id);
        if (userToUpdate == null) {
            throw new ResourceNotFoundException("User " + id + " not found");
        }
        user.setId(id);
        return ResponseEntity.ok(UserService.updateUser(user));

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