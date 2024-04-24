package com.capstonedk.Maven.repository;

import java.util.Optional;
import com.capstonedk.Maven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginIdAndPassword(String loginId, String password);
}