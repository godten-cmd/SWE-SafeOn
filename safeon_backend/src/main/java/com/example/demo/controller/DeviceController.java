package com.example.demo.controller;

import com.example.demo.dto.ApiResponseDto;
import com.example.demo.dto.SimpleMessageResponseDto;
import com.example.demo.dto.device.DeviceRegisterRequestDto;
import com.example.demo.dto.device.DeviceResponseDto;
import com.example.demo.security.AuthContext;
import com.example.demo.service.DeviceService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/devices")
@Tag(name = "Device Page")
public class DeviceController {

    private final DeviceService deviceService;
    private final AuthContext authContext;

    @GetMapping
    public ResponseEntity<ApiResponseDto<java.util.List<DeviceResponseDto>>> getDevices(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Tenant-Id") String tenantId
    ) {
        AuthContext.TenantSession session = authContext.requireSession(userId, tenantId);
        java.util.List<DeviceResponseDto> data = deviceService.getDevices(session);
        return ResponseEntity.ok(ApiResponseDto.ok(data));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<DeviceResponseDto>> registerDevice(
            @Valid @RequestBody DeviceRegisterRequestDto request) {

        AuthContext.TenantSession session = authContext.requireSession(request.getUserId(), request.getTenantId());
        DeviceResponseDto data = deviceService.registerDevice(request, session);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.ok(data));
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<ApiResponseDto<DeviceResponseDto>> getDevice(
            @PathVariable String deviceId,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Tenant-Id") String tenantId
    ) {
        AuthContext.TenantSession session = authContext.requireSession(userId, tenantId);
        DeviceResponseDto data = deviceService.getDevice(deviceId, session);
        return ResponseEntity.ok(ApiResponseDto.ok(data));
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<ApiResponseDto<SimpleMessageResponseDto>> deleteDevice(
            @PathVariable String deviceId,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Tenant-Id") String tenantId
    ) {
        AuthContext.TenantSession session = authContext.requireSession(userId, tenantId);
        deviceService.deleteDevice(deviceId, session);
        return ResponseEntity.ok(ApiResponseDto.ok(
                SimpleMessageResponseDto.of("device deleted: " + deviceId)
        ));
    }
}
