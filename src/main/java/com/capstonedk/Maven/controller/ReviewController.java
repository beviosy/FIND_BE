package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.model.request.ReviewCreationRequest;
import com.capstonedk.Maven.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@Tag(name = "Review", description = "리뷰 API")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "리뷰 작성", description = "새로운 리뷰 작성")
    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewCreationRequest request) {
        try {
            Review createdReview = reviewService.createReview(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "리뷰 조회", description = "특정 리뷰 조회")
    @GetMapping("/read/{reviewId}")
    public ResponseEntity<Review> readReview(@PathVariable Long reviewId) {
        try {
            Review review = reviewService.findReview(reviewId);
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "리뷰 수정", description = "기존 리뷰 수정")
    @PutMapping("/update/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId, @RequestBody ReviewCreationRequest request) {
        try {
            Review updatedReview = reviewService.updateReview(reviewId, request);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "리뷰 삭제", description = "특정 리뷰 삭제")
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
