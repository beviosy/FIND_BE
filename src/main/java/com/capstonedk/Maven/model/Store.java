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
    // Category 엔티티를 참조하도록 수정합니다.
    @JoinColumn(name = "category_id")
    private Locale.Category categoryId; // 한식 1, 중식 2, 양식 3, 일식 4

    @Column(columnDefinition = "TEXT", nullable = false)
    private String info;

    @OneToMany(mappedBy = "store")
    private List<Review> reviews;
}