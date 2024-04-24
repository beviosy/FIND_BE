package com.capstonedk.Maven.model.request;

import com.capstonedk.Maven.model.Store;
import com.capstonedk.Maven.model.User;
import lombok.Data;

import java.time.Instant;

@Data
public class ReviewCreationRequest {

    private Long reviewId;
    private User userId;
    private Store storeId;
    private int rating;
    private String content;
    private Instant created_date;
    private Instant modified_date;

}
