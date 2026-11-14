package com.essadany.localadvisor.service;

import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.model.Review;
import com.essadany.localadvisor.model.User;
import com.essadany.localadvisor.repository.PlaceRepository;
import com.essadany.localadvisor.repository.ReviewRepository;
import com.essadany.localadvisor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public Review addReview(Review review) {
        User user = userRepository.findById(review.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User " + review.getUser().getId() + " not found"));
        Place place = placeRepository.findById(review.getPlace().getPlaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Place " + review.getPlace().getPlaceId() + " not found"));
        review.setUser(user);
        review.setPlace(place);
        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsByPlace(Long placeId) {
        return reviewRepository.findByPlacePlaceId(placeId);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void deleteAllReviews() {
        reviewRepository.deleteAll();
    }
}
