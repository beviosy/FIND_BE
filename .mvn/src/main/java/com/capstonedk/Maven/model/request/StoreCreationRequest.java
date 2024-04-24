package com.capstonedk.Maven.model.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class StoreCreationRequest {
    private Long storeId;
    private String store_picture_url;
    private String store_name;
    private String store_address;
    private String store_phone_number;
    private float latitude;
    private float longitude;
    private float rating_average;
    private int categoryId;//한식 1, 중식 2, 양식 3, 일식 4
    private String info;
}
