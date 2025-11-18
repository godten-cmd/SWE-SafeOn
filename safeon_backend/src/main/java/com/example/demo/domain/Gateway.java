package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "gateways")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gateway {

    @Id
    @Column(name = "gateway_id")
    @GeneratedValue(generator = "uuid2")
    private UUID gatewayId;

    private String name;

    @Column(unique = true)
    private String serial;

    @Column(name = "mac_addr")
    private String macAddr;

    private String model;

    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "registration_at")
    private OffsetDateTime registrationAt;

    @Column(name = "last_seen")
    private OffsetDateTime lastSeen;

    public static Gateway create(
            String name,
            String serial,
            String macAddr,
            String model,
            String publicKey,
            OffsetDateTime registrationAt,
            OffsetDateTime lastSeen
    ) {
        Gateway gateway = new Gateway();
        gateway.name = name;
        gateway.serial = serial;
        gateway.macAddr = macAddr;
        gateway.model = model;
        gateway.publicKey = publicKey;
        gateway.registrationAt = registrationAt;
        gateway.lastSeen = lastSeen;
        return gateway;
    }
}
