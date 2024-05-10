package com.capstonedk.Maven.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String storePictureUrl;

    private String storeName;
    private String storeAddress;
    private String storePhoneNumber;
    private float latitude;
    private float longitude;
    private float ratingAverage;
    private int categoryId; // 한식 1, 중식 2, 양식 3, 일식 4

    @Column(columnDefinition = "TEXT", nullable = false)
    private String info;

    @OneToMany(mappedBy = "store")
    private List<Review> reviews;

    // 리뷰에서 평점의 평균을 계산하여 맛집의 ratingAverage에 업데이트
    public void updateRatingAverage() {
        if (reviews == null || reviews.isEmpty()) {
            ratingAverage = 0; // 리뷰가 없으면 평균은 0
            return;
        }

        int sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }

        ratingAverage = (float) sum / reviews.size();
    }
}
