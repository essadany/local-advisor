package com.essadany.localadvisor.service;

import com.essadany.localadvisor.exception.BadRequestException;
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

    public Review createReview(Review review) {
        if (review.getUser() == null || review.getUser().getId() == null) {
            throw new BadRequestException("A review requires a user id");
        }
        if (review.getPlace() == null || review.getPlace().getPlaceId() == null) {
            throw new BadRequestException("A review requires a place id");
        }
        User user = userRepository.findById(review.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User " + review.getUser().getId() + " not found"));
        Place place = placeRepository.findById(review.getPlace().getPlaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Place " + review.getPlace().getPlaceId() + " not found"));
        review.setReviewId(null);
        review.setUser(user);
        review.setPlace(place);
        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviews(Long placeId, Long userId) {
        if (placeId != null) {
            return reviewRepository.findByPlacePlaceId(placeId);
        }
        if (userId != null) {
            return reviewRepository.findByUserId(userId);
        }
        return reviewRepository.findAll();
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public Review updateReview(Long id, Review update) {
        Review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review " + id + " not found"));

        existing.setContent(update.getContent());
        existing.setRating(update.getRating());

        if (update.getUser() != null && update.getUser().getId() != null) {
            existing.setUser(userRepository.findById(update.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User " + update.getUser().getId() + " not found")));
        }
        if (update.getPlace() != null && update.getPlace().getPlaceId() != null) {
            existing.setPlace(placeRepository.findById(update.getPlace().getPlaceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Place " + update.getPlace().getPlaceId() + " not found")));
        }

        return reviewRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }
}
