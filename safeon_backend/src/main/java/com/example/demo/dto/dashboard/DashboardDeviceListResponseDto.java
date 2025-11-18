package com.example.demo.dto.dashboard;

import java.util.List;

public record DashboardDeviceListResponseDto(
        List<DashboardDeviceDto> devices
) {
}
