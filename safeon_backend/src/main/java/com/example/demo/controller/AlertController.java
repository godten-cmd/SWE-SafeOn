package com.example.demo.controller;

import com.example.demo.dto.ApiResponseDto;
import com.example.demo.dto.alert.AckRequestDto;
import com.example.demo.dto.alert.AlertResponseDto;
import com.example.demo.security.AuthContext;
import com.example.demo.service.AlertService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/alerts")
@Tag(name = "Alerts")
public class AlertController {

    private final AlertService alertService;
    private final AuthContext authContext;

    // 모든 로그 조회
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<AlertResponseDto>>> getAlerts(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Tenant-Id") String tenantId
    ) {
        AuthContext.TenantSession session = authContext.requireSession(userId, tenantId);
        List<AlertResponseDto> alerts = alertService.getAlerts(session);
        return ResponseEntity.ok(ApiResponseDto.ok(alerts));
    }

    // 로그 상세 조회
    @GetMapping("/{alertId}")
    public ResponseEntity<ApiResponseDto<AlertResponseDto>> getAlert(
            @PathVariable String alertId,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Tenant-Id") String tenantId
    ) {
        AuthContext.TenantSession session = authContext.requireSession(userId, tenantId);
        AlertResponseDto alert = alertService.getAlert(alertId, session);
        return ResponseEntity.ok(ApiResponseDto.ok(alert));
    }

    // 로그 확인 여부
    @PostMapping("/{alertId}/ack")
    public ResponseEntity<ApiResponseDto<AlertResponseDto>> acknowledgeAlert(
            @PathVariable String alertId,
            @Valid @RequestBody AckRequestDto request
    ) {
        AuthContext.TenantSession session = authContext.requireSession(request.getUserId(), request.getTenantId());
        AlertResponseDto alert = alertService.acknowledgeAlert(alertId, session);
        return ResponseEntity.ok(ApiResponseDto.ok(alert));
    }
}
