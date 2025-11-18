package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "anomaly_scores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnomalyScore {

    @Id
    @Column(name = "score_id")
    @GeneratedValue(generator = "uuid2")
    private UUID scoreId;

    private OffsetDateTime ts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id")
    private Flow flow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name = "iso_score")
    private Double isoScore;

    @Column(name = "iso_threshold")
    private Double isoThreshold;

    @Column(name = "is_iso")
    private Boolean isIso;

    @Column(name = "lstm_score")
    private Double lstmScore;

    @Column(name = "lstm_threshold")
    private Double lstmThreshold;

    @Column(name = "is_lstm")
    private Boolean isLstm;

    @Column(name = "hybrid_score")
    private Double hybridScore;

    @Column(name = "is_anom")
    private Boolean isAnom;

    public static AnomalyScore create(
            OffsetDateTime ts,
            Flow flow,
            Tenant tenant,
            Double isoScore,
            Double isoThreshold,
            Boolean isIso,
            Double lstmScore,
            Double lstmThreshold,
            Boolean isLstm,
            Double hybridScore,
            Boolean isAnom
    ) {
        AnomalyScore score = new AnomalyScore();
        score.ts = ts;
        score.flow = flow;
        score.tenant = tenant;
        score.isoScore = isoScore;
        score.isoThreshold = isoThreshold;
        score.isIso = isIso;
        score.lstmScore = lstmScore;
        score.lstmThreshold = lstmThreshold;
        score.isLstm = isLstm;
        score.hybridScore = hybridScore;
        score.isAnom = isAnom;
        return score;
    }
}
