package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Favorite;
import com.essadany.localadvisor.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorites")
class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/{userId}")
    public ResponseEntity<Iterable<Favorite>> getFavoritesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Favorite> addFavorite(@RequestBody Favorite favorite) {
        Favorite createdFavorite = favoriteService.addFavorite(favorite);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFavorite);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFavoriteById(@PathVariable Long id) {
        if (favoriteService.getFavoriteById(id).isEmpty()) {
            throw new ResourceNotFoundException("Favorite " + id + " not found");
        }
        favoriteService.removeFavoriteById(id);
        return ResponseEntity.noContent().build();
    }

}