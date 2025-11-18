package com.example.demo.domain;

import com.example.demo.exception.TenantAccessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "alerts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alert {

    @Id
    @Column(name = "alert_id")
    @GeneratedValue(generator = "uuid2")
    private UUID alertId;

    private OffsetDateTime ts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id")
    private Flow flow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    private String severity;
    private String reason;

    @Column(columnDefinition = "jsonb")
    private String evidence;

    private String status;

    public static Alert create(
            OffsetDateTime ts,
            Flow flow,
            Tenant tenant,
            Device device,
            String severity,
            String reason,
            String evidence,
            String status
    ) {
        Alert alert = new Alert();
        alert.ts = ts;
        alert.flow = flow;
        alert.tenant = tenant;
        alert.device = device;
        alert.severity = severity;
        alert.reason = reason;
        alert.evidence = evidence;
        alert.status = status;
        return alert;
    }

    public void acknowledge() {
        this.status = "ACKNOWLEDGED";
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void ensureTenant(UUID tenantId) {
        UUID currentTenant = tenant != null ? tenant.getTenantId() : null;
        if (currentTenant == null || !currentTenant.equals(tenantId)) {
            throw new TenantAccessException("Alert does not belong to tenant: " + tenantId);
        }
    }

    public void ensureDevice(Device expectedDevice) {
        if (device == null || !device.getDeviceId().equals(expectedDevice.getDeviceId())) {
            throw new TenantAccessException("Alert does not belong to the provided device.");
        }
    }
}
