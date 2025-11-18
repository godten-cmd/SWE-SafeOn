package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_tenants")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTenant {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name = "joined_at")
    private OffsetDateTime joinedAt;

    public static UserTenant create(User user, Tenant tenant, OffsetDateTime joinedAt) {
        UserTenant userTenant = new UserTenant();
        userTenant.user = user;
        userTenant.tenant = tenant;
        userTenant.joinedAt = joinedAt;
        return userTenant;
    }
}
