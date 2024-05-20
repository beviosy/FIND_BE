package com.capstonedk.Maven.dto;

import com.capstonedk.Maven.model.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ReviewDTO {
    private Long reviewId;
    private Long userId;
    private Long storeId;
    private int rating;
    private String content;
    private Instant createdDate;
    private Instant modifiedDate;

    public ReviewDTO(Review review) {
        this.reviewId = review.getReviewId();
        this.userId = review.getUser().getUserId();
        this.storeId = review.getStore().getStoreId();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.createdDate = review.getCreatedDate();
        this.modifiedDate = review.getModifiedDate();
    }
}