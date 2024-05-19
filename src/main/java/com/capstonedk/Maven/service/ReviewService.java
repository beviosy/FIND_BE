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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public Review createReview(ReviewCreationRequest request, String userId) {
        Optional<User> userOptional = userRepository.findByLoginId(userId);
        Optional<Store> storeOptional = storeRepository.findById(request.getStoreId());

        if (userOptional.isPresent() && storeOptional.isPresent()) {
            Review review = new Review();
            review.setUser(userOptional.get());
            review.setStore(storeOptional.get());
            review.setRating(request.getRating());
            review.setContent(request.getContent());
            review.setCreatedDate(Instant.now());
            review.setModifiedDate(Instant.now());

            return reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("Invalid userId or storeId");
        }
    }

    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    }

    public Review updateReview(Long reviewId, ReviewCreationRequest request) {
        Review review = findReview(reviewId);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setModifiedDate(Instant.now());

        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        Review review = findReview(reviewId);
        reviewRepository.delete(review);
    }

    public List<Review> findReviewsByUserId(Long userId) {
        return reviewRepository.findByUserUserId(userId);
    }

    public List<Review> findReviewsByStoreId(Long storeId) {
        return reviewRepository.findByStoreStoreId(storeId);
    }
}
