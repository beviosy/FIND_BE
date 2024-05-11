package com.capstonedk.Maven.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id") // review_id와 매핑되는 데이터베이스 열을 지정합니다.
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId") // User 엔티티의 프라이머리 키와 연결되는 외래 키
    @JsonManagedReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId") // Store 엔티티의 프라이머리 키와 연결되는 외래 키
    @JsonManagedReference
    private Store store;

    private int rating;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Instant createdDate;
    private Instant modifiedDate;
}
