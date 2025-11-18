package com.example.demo.repository;

import com.example.demo.domain.UserTenant;
import com.example.demo.exception.TenantAccessException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTenantRepository extends JpaRepository<UserTenant, UUID> {
    List<UserTenant> findByUserUserId(UUID userId);

    Optional<UserTenant> findByUserUserIdAndTenantTenantId(UUID userId, UUID tenantId);

    boolean existsByUserUserIdAndTenantTenantId(UUID userId, UUID tenantId);

    default void validateUserBelongsToTenant(UUID userId, UUID tenantId) {
        if (!existsByUserUserIdAndTenantTenantId(userId, tenantId)) {
            throw new TenantAccessException("User " + userId + " is not part of tenant " + tenantId);
        }
    }

    default UserTenant getUserTenant(UUID userId, UUID tenantId) {
        return findByUserUserIdAndTenantTenantId(userId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("사용자-테넌트 관계를 찾을 수 없습니다."));
    }
}
