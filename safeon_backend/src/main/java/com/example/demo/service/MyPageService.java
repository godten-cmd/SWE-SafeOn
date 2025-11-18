package com.example.demo.service;

import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.dto.user.UserUpdateRequestDto;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserTenantRepository;
import com.example.demo.security.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final UserTenantRepository userTenantRepository;

    @Transactional(readOnly = true)
    public UserResponseDto getProfile(AuthContext.TenantSession session) {
        UUID userId = session.userId();
        UUID tenantId = session.tenantId();
        validateUserTenant(userId, tenantId);
        User user = getUser(userId);
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto updateProfile(UserUpdateRequestDto request, AuthContext.TenantSession session) {
        UUID userId = session.userId();
        UUID tenantId = session.tenantId();
        validateUserTenant(userId, tenantId);
        User user = getUser(userId);

        user.updateProfile(request.getName(), request.getPassword());
        return UserResponseDto.from(userRepository.save(user));
    }

    private void validateUserTenant(UUID userId, UUID tenantId) {
        userTenantRepository.validateUserBelongsToTenant(userId, tenantId);
    }

    private User getUser(UUID userId) {
        return userRepository.getById(userId);
    }
}
