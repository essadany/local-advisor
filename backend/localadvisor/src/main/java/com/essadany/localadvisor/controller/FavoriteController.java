package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.model.Favorite;
import com.essadany.localadvisor.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(createdFavorite);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFavoriteById(@PathVariable Long id) {
        favoriteService.removeFavoriteById(id);
        return ResponseEntity.ok("Favorite removed successfully");
    }

}