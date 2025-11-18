package com.example.demo.dto.dashboard;

import com.example.demo.dto.alert.AlertResponseDto;

import java.util.List;

public record RecentAlertListResponseDto(
        List<AlertResponseDto> alerts
) {
}
