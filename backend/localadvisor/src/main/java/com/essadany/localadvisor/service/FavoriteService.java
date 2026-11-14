package com.essadany.localadvisor.service;

import com.essadany.localadvisor.exception.ConflictException;
import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Favorite;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.model.User;
import com.essadany.localadvisor.repository.FavoriteRepository;
import com.essadany.localadvisor.repository.PlaceRepository;
import com.essadany.localadvisor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<Place> getFavoritePlaces(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));

        List<Place> places = new ArrayList<>();
        favoriteRepository.findAllByUser(user).forEach(favorite -> places.add(favorite.getPlace()));
        return places;
    }

    public Favorite addFavorite(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place " + placeId + " not found"));

        if (favoriteRepository.existsByUserAndPlace(user, place)) {
            throw new ConflictException("Place " + placeId + " is already a favorite of user " + userId);
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setPlace(place);
        favorite.setDateAdded(new java.util.Date());
        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place " + placeId + " not found"));

        favoriteRepository.deleteByUserAndPlace(user, place);
    }
}
