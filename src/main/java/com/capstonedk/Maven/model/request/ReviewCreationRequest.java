package com.capstonedk.Maven.model.request;

import lombok.Data;

import java.time.Instant;

@Data
public class ReviewCreationRequest {
    private Long storeId;
    private int rating;
    private String content;
    private Instant createdDate;
    private Instant modifiedDate;
}
