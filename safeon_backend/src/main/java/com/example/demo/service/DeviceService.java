package com.example.demo.service;

import com.example.demo.dto.device.DeviceRegisterRequestDto;
import com.example.demo.dto.device.DeviceResponseDto;
import com.example.demo.domain.Device;
import com.example.demo.domain.Tenant;
import com.example.demo.domain.User;
import com.example.demo.domain.UserDevice;
import com.example.demo.exception.TenantAccessException;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.repository.TenantRepository;
import com.example.demo.repository.UserDeviceRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserTenantRepository;
import com.example.demo.security.AuthContext;
import com.example.demo.util.UuidParser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private static final String STATUS_UNKNOWN = "UNKNOWN";

    private final DeviceRepository deviceRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final UserTenantRepository userTenantRepository;

    @Transactional
    public DeviceResponseDto registerDevice(DeviceRegisterRequestDto request, AuthContext.TenantSession session) {
        UUID userId = session.userId();
        UUID tenantId = session.tenantId();
        validateUserBelongsToTenant(userId, tenantId);
        User user = getUser(userId);
        Tenant tenant = getTenant(tenantId);

        Device device = Device.create(
                tenant,
                request.getVendor(),
                request.getModel(),
                request.getMacAddr(),
                request.getCategory(),
                STATUS_UNKNOWN,
                OffsetDateTime.now()
        );

        Device savedDevice = deviceRepository.save(device);
        userDeviceRepository.save(UserDevice.create(user, savedDevice, request.getModel(), OffsetDateTime.now()));

        return DeviceResponseDto.from(savedDevice, userId);
    }

    @Transactional
    public void deleteDevice(String deviceId, AuthContext.TenantSession session) {
        UUID userId = session.userId();
        UUID tenantId = session.tenantId();
        validateUserBelongsToTenant(userId, tenantId);
        Device device = getDeviceEntity(deviceId, tenantId);
        device.ensureTenant(tenantId);
        ensureUserHasDevice(device.getDeviceId(), userId, tenantId);
        userDeviceRepository.findAllByDeviceDeviceIdAndDeviceTenantTenantId(device.getDeviceId(), tenantId)
                .forEach(userDeviceRepository::delete);
        deviceRepository.delete(device);
    }

    @Transactional(readOnly = true)
    public DeviceResponseDto getDevice(String deviceId, AuthContext.TenantSession session) {
        UUID userId = session.userId();
        UUID tenantId = session.tenantId();
        validateUserBelongsToTenant(userId, tenantId);
        Device device = getDeviceEntity(deviceId, tenantId);
        device.ensureTenant(tenantId);
        UUID ownerId = userDeviceRepository.findFirstByDeviceDeviceIdAndDeviceTenantTenantId(device.getDeviceId(), tenantId)
                .map(ud -> ud.getUser().getUserId())
                .orElse(null);
        return DeviceResponseDto.from(device, ownerId);
    }

    @Transactional(readOnly = true)
    public List<DeviceResponseDto> getDevices(AuthContext.TenantSession session) {
        UUID userId = session.userId();
        UUID tenantId = session.tenantId();
        validateUserBelongsToTenant(userId, tenantId);
        return userDeviceRepository.findAllByUserUserIdAndDeviceTenantTenantId(userId, tenantId)
                .stream()
                .map(DeviceResponseDto::from)
                .toList();
    }

    private void validateUserBelongsToTenant(UUID userId, UUID tenantId) {
        userTenantRepository.validateUserBelongsToTenant(userId, tenantId);
    }

    private Device getDeviceEntity(String deviceId, UUID tenantId) {
        UUID deviceUuid = UuidParser.parseUUID(deviceId);
        return deviceRepository.getByDeviceIdAndTenantTenantId(deviceUuid, tenantId);
    }

    private void ensureUserHasDevice(UUID deviceId, UUID userId, UUID tenantId) {
        UserDevice userDevice = userDeviceRepository.getByDeviceAndUserAndTenant(deviceId, userId, tenantId);
        userDevice.ensureOwner(userId);
        userDevice.ensureTenant(tenantId);
    }

    private User getUser(UUID userId) {
        return userRepository.getById(userId);
    }

    private Tenant getTenant(UUID tenantId) {
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("테넌트를 찾을 수 없습니다 : " + tenantId));
    }
}
