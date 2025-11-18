package com.example.demo.repository;

import com.example.demo.domain.UserDevice;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {
    List<UserDevice> findAllByUserUserIdAndDeviceTenantTenantId(UUID userId, UUID tenantId);

    Optional<UserDevice> findByDeviceDeviceIdAndUserUserIdAndDeviceTenantTenantId(UUID deviceId, UUID userId, UUID tenantId);

    long countByUserUserIdAndDeviceTenantTenantId(UUID userId, UUID tenantId);

    long countByUserUserIdAndDeviceStatusAndDeviceTenantTenantId(UUID userId, String status, UUID tenantId);

    List<UserDevice> findAllByDeviceDeviceIdAndDeviceTenantTenantId(UUID deviceId, UUID tenantId);

    Optional<UserDevice> findFirstByDeviceDeviceIdAndDeviceTenantTenantId(UUID deviceId, UUID tenantId);

    default UserDevice getByDeviceAndUserAndTenant(UUID deviceId, UUID userId, UUID tenantId) {
        return findByDeviceDeviceIdAndUserUserIdAndDeviceTenantTenantId(deviceId, userId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자와 디바이스가 연결되지 않았습니다."));
    }

    default UserDevice getFirstByDeviceAndTenant(UUID deviceId, UUID tenantId) {
        return findFirstByDeviceDeviceIdAndDeviceTenantTenantId(deviceId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("디바이스 연결 정보를 찾을 수 없습니다."));
    }
}
