package com.example.demo.repository;

import com.example.demo.domain.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    default User getById(UUID userId) {
        return findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다 : " + userId));
    }

    default User getByEmail(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다 : " + email));
    }
}
