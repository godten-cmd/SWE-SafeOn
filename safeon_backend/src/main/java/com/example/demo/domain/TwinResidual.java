package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "twin_residuals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TwinResidual {

    @Id
    @Column(name = "residual_id")
    @GeneratedValue(generator = "uuid2")
    private UUID residualId;

    private OffsetDateTime ts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id")
    private Flow flow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name = "twin_ver")
    private String twinVer;

    private Double pred;
    private Double actual;
    private Double residual;

    private String state;

    public static TwinResidual create(
            OffsetDateTime ts,
            Flow flow,
            Tenant tenant,
            String twinVer,
            Double pred,
            Double actual,
            Double residual,
            String state
    ) {
        TwinResidual entity = new TwinResidual();
        entity.ts = ts;
        entity.flow = flow;
        entity.tenant = tenant;
        entity.twinVer = twinVer;
        entity.pred = pred;
        entity.actual = actual;
        entity.residual = residual;
        entity.state = state;
        return entity;
    }
}
