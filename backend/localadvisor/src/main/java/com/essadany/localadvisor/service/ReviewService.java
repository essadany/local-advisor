package com.essadany.localadvisor.service;

import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.model.Review;
import com.essadany.localadvisor.model.User;
import com.essadany.localadvisor.repository.PlaceRepository;
import com.essadany.localadvisor.repository.ReviewRepository;
import com.essadany.localadvisor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaceRepository placeRepository;

    public Review addReview(Review review) {
        User user = userRepository.findById(review.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Place place = placeRepository.findById(review.getPlace().getPlaceId())
                .orElseThrow(() -> new RuntimeException("Place not found"));
        review.setUser(user);
        review.setPlace(place);
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<Review> getReviewsByPlace(Long placeId) {
        return reviewRepository.findByPlacePlaceId(placeId);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void deleteAllReviews() {
        reviewRepository.deleteAll();
    }
}
