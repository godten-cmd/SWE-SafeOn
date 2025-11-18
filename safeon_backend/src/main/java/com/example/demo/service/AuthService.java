package com.example.demo.service;

import com.example.demo.dto.user.LoginRequestDto;
import com.example.demo.dto.user.SignUpRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.domain.Tenant;
import com.example.demo.domain.User;
import com.example.demo.domain.UserTenant;
import com.example.demo.repository.TenantRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserTenantRepository;
import com.example.demo.util.UuidParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final UserTenantRepository userTenantRepository;

    @Transactional
    public UserResponseDto signup(SignUpRequestDto req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Tenant tenant = tenantRepository.save(
                Tenant.create(
                        null,
                        req.getName() != null ? req.getName() + " Tenant" : "SafeOn Tenant",
                        OffsetDateTime.now(),
                        null
                )
        );

        User user = User.create(
                req.getEmail(),
                req.getPassword(),
                req.getName(),
                tenant,
                OffsetDateTime.now()
        );
        User saved = userRepository.save(user);

        userTenantRepository.save(UserTenant.create(saved, tenant, OffsetDateTime.now()));

        return UserResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public UserResponseDto login(LoginRequestDto req) {
        User user = userRepository.getByEmail(req.getEmail());
        if (!user.getPassword().equals(req.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
        return UserResponseDto.from(user);
    }

    @Transactional(readOnly = true)
    public void logout(String userId) {
        if (userId == null || userId.isBlank()) {
            return;
        }
        UUID uuid = UuidParser.parseUUID(userId);
        if (!userRepository.existsById(uuid)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.: " + userId);
        }
    }
}
