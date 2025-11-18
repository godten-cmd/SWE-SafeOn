package com.example.demo.repository;

import com.example.demo.domain.UserAlert;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAlertRepository extends JpaRepository<UserAlert, UUID> {
    List<UserAlert> findByUserUserIdAndAlertTenantTenantIdOrderByNotifiedAtDesc(UUID userId, UUID tenantId);

    long countByUserUserIdAndAlertTenantTenantId(UUID userId, UUID tenantId);

    Optional<UserAlert> findByUserUserIdAndAlertAlertIdAndAlertTenantTenantId(UUID userId, UUID alertId, UUID tenantId);

    Optional<UserAlert> findFirstByUserUserIdAndAlertTenantTenantIdOrderByNotifiedAtDesc(UUID userId, UUID tenantId);

    Optional<UserAlert> findFirstByAlertAlertIdAndAlertTenantTenantId(UUID alertId, UUID tenantId);

    default UserAlert getByUserUserIdAndAlertAlertIdAndAlertTenantTenantId(UUID userId, UUID alertId, UUID tenantId) {
        return findByUserUserIdAndAlertAlertIdAndAlertTenantTenantId(userId, alertId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자와 연관된 알림이 없습니다."));
    }

    default UserAlert getLatestByUserAndTenant(UUID userId, UUID tenantId) {
        return findFirstByUserUserIdAndAlertTenantTenantIdOrderByNotifiedAtDesc(userId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("최근 알림을 찾을 수 없습니다."));
    }

    default UserAlert getByAlertIdAndTenant(UUID alertId, UUID tenantId) {
        return findFirstByAlertAlertIdAndAlertTenantTenantId(alertId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));
    }
}
