package com.example.demo.controller;

import com.example.demo.dto.ApiResponseDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.dto.user.UserUpdateRequestDto;
import com.example.demo.security.AuthContext;
import com.example.demo.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;
    private final AuthContext authContext;

    @GetMapping
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getProfile(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Tenant-Id") String tenantId
    ) {
        AuthContext.TenantSession session = authContext.requireSession(userId, tenantId);
        UserResponseDto profile = myPageService.getProfile(session);
        return ResponseEntity.ok(ApiResponseDto.ok(profile));
    }

    @PatchMapping
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateProfile(
            @Valid @RequestBody UserUpdateRequestDto request
    ) {
        AuthContext.TenantSession session = authContext.requireSession(request.getUserId(), request.getTenantId());
        UserResponseDto updated = myPageService.updateProfile(request, session);
        return ResponseEntity.ok(ApiResponseDto.ok(updated));
    }
}
