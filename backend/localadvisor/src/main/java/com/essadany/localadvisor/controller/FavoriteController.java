package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.dto.FavoriteRequest;
import com.essadany.localadvisor.model.Favorite;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping
    public List<Place> getFavorites(@PathVariable Long userId) {
        return favoriteService.getFavoritePlaces(userId);
    }

    @PostMapping
    public ResponseEntity<Place> addFavorite(@PathVariable Long userId, @Valid @RequestBody FavoriteRequest request) {
        Favorite created = favoriteService.addFavorite(userId, request.placeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created.getPlace());
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long userId, @PathVariable Long placeId) {
        favoriteService.removeFavorite(userId, placeId);
        return ResponseEntity.noContent().build();
    }
}
