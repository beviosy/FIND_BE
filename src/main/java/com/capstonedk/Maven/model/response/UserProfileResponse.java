package com.capstonedk.Maven.model.response;

import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private User user;
    private List<Review> reviews;
}
