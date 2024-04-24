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