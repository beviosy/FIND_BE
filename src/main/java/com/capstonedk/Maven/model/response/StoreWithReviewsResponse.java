package com.capstonedk.Maven.model.response;

import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.model.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreWithReviewsResponse {
    private Store store;
    private List<Review> reviews;
}

