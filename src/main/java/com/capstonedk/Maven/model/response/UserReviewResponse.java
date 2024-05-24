package com.capstonedk.Maven.model.response;

import com.capstonedk.Maven.dto.ReviewDTO;
import com.capstonedk.Maven.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserReviewResponse {
    private String loginId;
    private String nickname;
    private List<ReviewDTO> reviews;
    private int totalReviews;

    public UserReviewResponse(User user, List<ReviewDTO> reviews) {
        this.loginId = user.getLoginId();
        this.nickname = user.getNickname();
        this.reviews = reviews;
        this.totalReviews = reviews.size();
    }
}
