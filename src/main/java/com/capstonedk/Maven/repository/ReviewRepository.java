package com.capstonedk.Maven.repository;

import com.capstonedk.Maven.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
