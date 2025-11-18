package com.example.demo.dto.dashboard;

public record DashboardOverviewDto(
        int totalDevices,
        int onlineDevices,
        long alertCount,
        String lastAlertTime
) {
}
