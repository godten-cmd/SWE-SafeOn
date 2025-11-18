package com.example.demo.repository;

import com.example.demo.domain.Alert;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {
    Optional<Alert> findByAlertId(UUID alertId);

    Optional<Alert> findByAlertIdAndTenantTenantId(UUID alertId, UUID tenantId);

    default Alert getByAlertIdAndTenantTenantId(UUID alertId, UUID tenantId) {
        return findByAlertIdAndTenantTenantId(alertId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다 : " + alertId));
    }

    default Alert getByAlertId(UUID alertId) {
        return findByAlertId(alertId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다 : " + alertId));
    }
}
