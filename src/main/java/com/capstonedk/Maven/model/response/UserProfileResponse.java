package com.capstonedk.Maven.model.response;

import com.capstonedk.Maven.dto.ReviewDTO;
import com.capstonedk.Maven.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileResponse {
    private String loginId;
    private String nickname;
    private List<ReviewDTO> reviews;

    public UserProfileResponse(User user, List<ReviewDTO> reviews) {
        this.loginId = user.getLoginId();
        this.nickname = user.getNickname();
        this.reviews = reviews;
    }
}
