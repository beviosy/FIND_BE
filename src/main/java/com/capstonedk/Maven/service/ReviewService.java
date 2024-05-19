package com.capstonedk.Maven.service;

import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.model.Store;
import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.model.request.ReviewCreationRequest;
import com.capstonedk.Maven.repository.ReviewRepository;
import com.capstonedk.Maven.repository.StoreRepository;
import com.capstonedk.Maven.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public Review createReview(ReviewCreationRequest request, String userId) {
        User user = userRepository.findByLoginId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId"));
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid storeId"));

        Review review = new Review();
        review.setUser(user);
        review.setStore(store);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setCreatedDate(Instant.now());
        review.setModifiedDate(Instant.now());

        return reviewRepository.save(review);
    }

    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    }

    public Review updateReview(Long reviewId, ReviewCreationRequest request) {
        Review review = findReview(reviewId);
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid storeId"));

        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setStore(store);
        review.setModifiedDate(Instant.now());

        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        Review review = findReview(reviewId);
        reviewRepository.delete(review);
    }

    public List<Review> findReviewsByUserId(String userId) {
        User user = userRepository.findByLoginId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId"));
        return reviewRepository.findByUserUserId(user.getUserId());
    }

    public List<Review> findReviewsByStoreId(Long storeId) {
        return reviewRepository.findByStoreStoreId(storeId);
    }
}
