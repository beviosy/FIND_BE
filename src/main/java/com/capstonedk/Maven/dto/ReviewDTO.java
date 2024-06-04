package com.capstonedk.Maven.dto;

import com.capstonedk.Maven.model.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ReviewDTO {
    private Long reviewId;
    private String nickname;
    private Long storeId;
    private String StoreName;
    private int rating;
    private String content;
    private Instant createdDate;
    private Instant modifiedDate;

    public ReviewDTO(Review review) {
        this.reviewId = review.getReviewId();
        this.nickname = review.getUser().getNickname();
        this.storeId = review.getStore().getStoreId();
        this.StoreName = review.getStore().getStoreName();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.createdDate = review.getCreatedDate();
        this.modifiedDate = review.getModifiedDate();
    }
}