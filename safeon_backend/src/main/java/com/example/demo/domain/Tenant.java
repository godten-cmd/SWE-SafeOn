package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tenant {

    @Id
    @Column(name = "tenant_id")
    @GeneratedValue(generator = "uuid2")
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gateway_id")
    private Gateway gateway;

    private String name;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    public static Tenant create(Gateway gateway, String name, OffsetDateTime createdAt, User createdBy) {
        Tenant tenant = new Tenant();
        tenant.gateway = gateway;
        tenant.name = name;
        tenant.createdAt = createdAt;
        tenant.createdBy = createdBy;
        return tenant;
    }
}
