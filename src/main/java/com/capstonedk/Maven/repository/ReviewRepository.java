package com.capstonedk.Maven.repository;

import com.capstonedk.Maven.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserUserId(Long userId);
    List<Review> findByStoreStoreId(Long storeId); // 추가
}
