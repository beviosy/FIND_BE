package com.capstonedk.Maven.service;

import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.model.request.ReviewCreationRequest;
import com.capstonedk.Maven.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review createReview(ReviewCreationRequest request) {
        Review review = new Review();
        BeanUtils.copyProperties(request, review);
        // ReviewId 자동 증가
        review.setReviewId(reviewRepository.count() + 1);
        Review savedReview = reviewRepository.save(review);
        return savedReview;
    }

    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    public Review updateReview(Long reviewId, ReviewCreationRequest request) {
        Review reviewToUpdate = reviewRepository.findById(reviewId).orElse(null);
        if (reviewToUpdate != null) {
            // 업데이트 로직을 수행하고 저장
            // 예를 들어, request에서 필요한 정보를 가져와서 업데이트
            // 여기서는 간단히 BeanUtils.copyProperties를 사용하여 업데이트
            BeanUtils.copyProperties(request, reviewToUpdate);
            return reviewRepository.save(reviewToUpdate);
        } else {
            return null;
        }
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // 나머지 코드는 동일하게 유지됩니다.
}
