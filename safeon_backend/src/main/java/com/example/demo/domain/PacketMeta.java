package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "packet_meta")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PacketMeta {

    @Id
    @Column(name = "packet_id")
    @GeneratedValue(generator = "uuid2")
    private UUID packetId;

    private OffsetDateTime ts;

    @Column(name = "iface_id")
    private UUID ifaceId;

    @Column(name = "src_ip")
    private String srcIp;

    @Column(name = "dst_ip")
    private String dstIp;

    @Column(name = "src_port")
    private Short srcPort;

    @Column(name = "dst_port")
    private Short dstPort;

    @Column(name = "l3_proto")
    private String l3Proto;

    @Column(name = "l4_proto")
    private String l4Proto;

    @Column(name = "l7_proto")
    private String l7Proto;

    @Column(name = "size_bytes")
    private Integer sizeBytes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    public static PacketMeta create(
            OffsetDateTime ts,
            UUID ifaceId,
            String srcIp,
            String dstIp,
            Short srcPort,
            Short dstPort,
            String l3Proto,
            String l4Proto,
            String l7Proto,
            Integer sizeBytes,
            Tenant tenant
    ) {
        PacketMeta meta = new PacketMeta();
        meta.ts = ts;
        meta.ifaceId = ifaceId;
        meta.srcIp = srcIp;
        meta.dstIp = dstIp;
        meta.srcPort = srcPort;
        meta.dstPort = dstPort;
        meta.l3Proto = l3Proto;
        meta.l4Proto = l4Proto;
        meta.l7Proto = l7Proto;
        meta.sizeBytes = sizeBytes;
        meta.tenant = tenant;
        return meta;
    }
}
