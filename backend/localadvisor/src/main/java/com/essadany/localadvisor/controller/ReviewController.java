package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Review;
import com.essadany.localadvisor.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public List<Review> getReviews(
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false) Long userId) {
        return reviewService.getReviews(placeId, userId);
    }

    @GetMapping("/{id}")
    public Review getReview(@PathVariable Long id) {
        return reviewService.getReviewById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review created = reviewService.createReview(review);
        return ResponseEntity.created(URI.create("/api/v1/reviews/" + created.getReviewId())).body(created);
    }

    @PutMapping("/{id}")
    public Review updateReview(@PathVariable Long id, @RequestBody Review review) {
        getReview(id);
        return reviewService.updateReview(id, review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        getReview(id);
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
