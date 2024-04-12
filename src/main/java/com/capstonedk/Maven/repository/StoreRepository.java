package com.capstonedk.Maven.repository;

import com.capstonedk.Maven.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByCategoryId(int categoryId);
}
