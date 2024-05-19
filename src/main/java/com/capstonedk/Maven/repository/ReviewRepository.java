package com.capstonedk.Maven.repository;

import com.capstonedk.Maven.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserUserId(Long userId);

    // 가게 ID로 리뷰를 조회하는 메서드 추가
    List<Review> findByStoreStoreId(Long storeId);
}