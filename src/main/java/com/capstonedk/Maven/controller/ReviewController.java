package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.dto.ReviewDTO;
import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.model.request.ReviewCreationRequest;
import com.capstonedk.Maven.model.response.ApiResponse;
import com.capstonedk.Maven.service.ReviewService;
import com.capstonedk.Maven.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/review")
@Tag(name = "Review", description = "리뷰 API")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ReviewController(ReviewService reviewService, JwtUtil jwtUtil) {
        this.reviewService = reviewService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "리뷰 작성", description = "새로운 리뷰 작성")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createReview(HttpServletRequest request, @RequestBody ReviewCreationRequest reviewRequest) {
        String token = getToken(request);
        if (token == null || !isValidToken(token) || jwtUtil.isAccessTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "UNAUTHORIZED", "유효한 엑세스 토큰이 필요합니다.", null));
        }
        String userId = jwtUtil.getUsernameFromToken(token);
        try {
            Review createdReview = reviewService.createReview(reviewRequest, userId);
            ReviewDTO reviewDTO = new ReviewDTO(createdReview);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "REVIEW_CREATED", "리뷰가 성공적으로 작성되었습니다.", reviewDTO, createdReview.getReviewId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "INTERNAL_SERVER_ERROR", "리뷰 작성 중 오류가 발생했습니다.", null));
        }
    }

    @Operation(summary = "리뷰 조회", description = "특정 리뷰 조회")
    @GetMapping("/read/{reviewId}")
    public ResponseEntity<ApiResponse> readReview(@PathVariable Long reviewId) {
        try {
            Review review = reviewService.findReview(reviewId);
            ReviewDTO reviewDTO = new ReviewDTO(review);
            return ResponseEntity.ok(new ApiResponse(true, "REVIEW_FOUND", "리뷰가 성공적으로 조회되었습니다.", reviewDTO, review.getReviewId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "REVIEW_NOT_FOUND", "리뷰를 찾을 수 없습니다.", null));
        }
    }

    @Operation(summary = "리뷰 수정", description = "기존 리뷰 수정")
    @PutMapping("/update/{reviewId}")
    public ResponseEntity<ApiResponse> updateReview(HttpServletRequest request, @PathVariable Long reviewId, @RequestBody ReviewCreationRequest reviewRequest) {
        String token = getToken(request);
        if (token == null || !isValidToken(token) || jwtUtil.isAccessTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "UNAUTHORIZED", "유효한 엑세스 토큰이 필요합니다.", null));
        }
        String userId = jwtUtil.getUsernameFromToken(token);

        try {
            Review existingReview = reviewService.findReview(reviewId);
            if (!existingReview.getUser().getLoginId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "FORBIDDEN", "리뷰를 수정할 권한이 없습니다.", null));
            }

            Review updatedReview = reviewService.updateReview(reviewId, reviewRequest);
            ReviewDTO reviewDTO = new ReviewDTO(updatedReview);
            return ResponseEntity.ok(new ApiResponse(true, "REVIEW_UPDATED", "리뷰가 성공적으로 수정되었습니다.", reviewDTO, updatedReview.getReviewId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "INTERNAL_SERVER_ERROR", "리뷰 수정 중 오류가 발생했습니다.", null));
        }
    }

    @Operation(summary = "리뷰 삭제", description = "특정 리뷰 삭제")
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(HttpServletRequest request, @PathVariable Long reviewId) {
        String token = getToken(request);
        if (token == null || !isValidToken(token) || jwtUtil.isAccessTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "UNAUTHORIZED", "유효한 엑세스 토큰이 필요합니다.", null));
        }
        String userId = jwtUtil.getUsernameFromToken(token);

        try {
            Review existingReview = reviewService.findReview(reviewId);
            if (!existingReview.getUser().getLoginId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "FORBIDDEN", "리뷰를 삭제할 권한이 없습니다.", null));
            }

            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok(new ApiResponse(true, "REVIEW_DELETED", "리뷰가 성공적으로 삭제되었습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "INTERNAL_SERVER_ERROR", "리뷰 삭제 중 오류가 발생했습니다.", null));
        }
    }

    private boolean isValidToken(String token) {
        return token != null && jwtUtil.validateToken(token);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}