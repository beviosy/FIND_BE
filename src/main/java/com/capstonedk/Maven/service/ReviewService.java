package com.capstonedk.Maven.service;

import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.repository.ReviewRepository;
import com.capstonedk.Maven.model.request.ReviewCreationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // Review operations

    public Review createReview(ReviewCreationRequest request) {
        Review review = new Review();
        BeanUtils.copyProperties(request, review);
        return reviewRepository.save(review);
    }

    public Review updateReview(Long reviewId, ReviewCreationRequest request) {
        Review review = findReview(reviewId);
        BeanUtils.copyProperties(request, review);
        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        try {
            reviewRepository.deleteById(reviewId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EmptyResultDataAccessException("Cannot delete review with ID " + reviewId + " as it does not exist", 1);
        }
    }

    private Review findReview(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.orElseThrow(() -> new EmptyResultDataAccessException("Review not present in the database", 1));
    }
}