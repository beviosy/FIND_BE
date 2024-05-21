package com.capstonedk.Maven.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.capstonedk.Maven.dto.ReviewDTO;  // 추가된 import 문

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

    @Transient
    private List<ReviewDTO> reviewDTOs; // 유지된 필드

    // 리뷰에서 평점의 평균을 계산하여 맛집의 ratingAverage에 업데이트
    public void updateRatingAverage() {
        if (reviewDTOs == null || reviewDTOs.isEmpty()) {
            ratingAverage = 0; // 리뷰가 없으면 평균은 0
            return;
        }

        float sum = 0;
        for (ReviewDTO reviewDTO : reviewDTOs) {
            sum += reviewDTO.getRating();
        }

        ratingAverage = sum / reviewDTOs.size();
    }
}
