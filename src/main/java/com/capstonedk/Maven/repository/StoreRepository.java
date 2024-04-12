package com.capstonedk.Maven.repository;

import com.capstonedk.Maven.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByCategoryId(int categoryId);
}
