package com.essadany.localadvisor.service;

import com.essadany.localadvisor.exception.ConflictException;
import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Favorite;
import com.essadany.localadvisor.model.User;
import com.essadany.localadvisor.repository.FavoriteRepository;
import com.essadany.localadvisor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public void removeFavoriteById(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

    @Transactional(readOnly = true)
    public Iterable<Favorite> getFavoritesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        return favoriteRepository.findAllByUser(user);
    }

    @Transactional(readOnly = true)
    public Optional<Favorite> getFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId);
    }

    public Favorite addFavorite(Favorite favorite) {
        if (favoriteRepository.existsByUserAndPlace(favorite.getUser(), favorite.getPlace())) {
            throw new ConflictException("This place is already a favorite of this user");
        }
        return favoriteRepository.save(favorite);
    }
}
