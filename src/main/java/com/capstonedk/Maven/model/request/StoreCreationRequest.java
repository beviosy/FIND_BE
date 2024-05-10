package com.capstonedk.Maven.model.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class StoreCreationRequest {
    private String storePictureUrl;
    private String storeName;
    private String storeAddress;
    private String storePhoneNumber;
    private float latitude;
    private float longitude;
    private float ratingAverage;
    private int categoryId;//한식 1, 중식 2, 양식 3, 일식 4
    private String info;
}
