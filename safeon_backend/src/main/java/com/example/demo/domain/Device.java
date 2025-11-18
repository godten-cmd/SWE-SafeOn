package com.example.demo.domain;

import com.example.demo.exception.TenantAccessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "devices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device {

    @Id
    @Column(name = "device_id")
    @GeneratedValue(generator = "uuid2")
    private UUID deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    private String vendor;

    private String model;

    @Column(name = "mac_addr")
    private String macAddr;

    private String category;

    private String status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public static Device create(
            Tenant tenant,
            String vendor,
            String model,
            String macAddr,
            String category,
            String status,
            OffsetDateTime createdAt
    ) {
        Device device = new Device();
        device.tenant = tenant;
        device.vendor = vendor;
        device.model = model;
        device.macAddr = macAddr;
        device.category = category;
        device.status = status;
        device.createdAt = createdAt;
        return device;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void ensureTenant(UUID tenantId) {
        UUID currentTenant = tenant != null ? tenant.getTenantId() : null;
        if (currentTenant == null || !currentTenant.equals(tenantId)) {
            throw new TenantAccessException("Device does not belong to tenant: " + tenantId);
        }
    }
}
