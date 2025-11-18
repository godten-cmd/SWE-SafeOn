package com.example.demo.security;

import com.example.demo.repository.UserTenantRepository;
import com.example.demo.util.UuidParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthContext {

    private final UserTenantRepository userTenantRepository;

    public TenantSession requireSession(String userId, String tenantId) {
        UUID userUuid = UuidParser.parseUUID(userId);
        UUID tenantUuid = UuidParser.parseUUID(tenantId);
        validateUserBelongsToTenant(userUuid, tenantUuid);
        return new TenantSession(userUuid, tenantUuid);
    }

    public void validateUserBelongsToTenant(UUID userId, UUID tenantId) {
        userTenantRepository.validateUserBelongsToTenant(userId, tenantId);
    }

    public record TenantSession(UUID userId, UUID tenantId) {
    }
}
