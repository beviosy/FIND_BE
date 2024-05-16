package com.capstonedk.Maven.repository;

import java.util.Optional;
import com.capstonedk.Maven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByLoginIdAndPassword(String loginId, String password);

    Optional<User> findByNickname(String nickname);
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
}
