package com.example.demo.controller;

import com.example.demo.dto.ApiResponseDto;
import com.example.demo.dto.dashboard.DashboardDeviceListResponseDto;
import com.example.demo.dto.dashboard.DashboardOverviewDto;
import com.example.demo.dto.dashboard.RecentAlertListResponseDto;
import com.example.demo.security.AuthContext;
import com.example.demo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final AuthContext authContext;

    @GetMapping("/overview")
    public ResponseEntity<ApiResponseDto<DashboardOverviewDto>> getOverview(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Tenant-Id") String tenantId
    ) {
        AuthContext.TenantSession session = authContext.requireSession(userId, tenantId);
        DashboardOverviewDto overview = dashboardService.getOverview(session);
        return ResponseEntity.ok(ApiResponseDto.ok(overview));
    }

    @GetMapping("/devices")
    public ResponseEntity<ApiResponseDto<DashboardDeviceListResponseDto>> getDevices(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Tenant-Id") String tenantId
    ) {
        AuthContext.TenantSession session = authContext.requireSession(userId, tenantId);
        DashboardDeviceListResponseDto devices = dashboardService.getDevices(session);
        return ResponseEntity.ok(ApiResponseDto.ok(devices));
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponseDto<RecentAlertListResponseDto>> getRecentAlerts(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Tenant-Id") String tenantId,
            @RequestParam(required = false) Integer limit
    ) {
        AuthContext.TenantSession session = authContext.requireSession(userId, tenantId);
        RecentAlertListResponseDto alerts = dashboardService.getRecentAlerts(session, limit);
        return ResponseEntity.ok(ApiResponseDto.ok(alerts));
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Tenant-Id") String tenantId
    ) {
        AuthContext.TenantSession session = authContext.requireSession(userId, tenantId);
        return dashboardService.stream(session);
    }
}
