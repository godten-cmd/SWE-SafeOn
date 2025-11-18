package com.example.demo.service;

import com.example.demo.dto.alert.AlertResponseDto;
import com.example.demo.dto.dashboard.DashboardDeviceDto;
import com.example.demo.dto.dashboard.DashboardDeviceListResponseDto;
import com.example.demo.dto.dashboard.DashboardOverviewDto;
import com.example.demo.dto.dashboard.RecentAlertListResponseDto;
import com.example.demo.domain.UserAlert;
import com.example.demo.repository.UserAlertRepository;
import com.example.demo.repository.UserDeviceRepository;
import com.example.demo.repository.UserTenantRepository;
import com.example.demo.security.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final String STATUS_ONLINE = "ONLINE";
    private static final long STREAM_TIMEOUT_MILLIS = Duration.ofMinutes(5).toMillis();

    private final UserDeviceRepository userDeviceRepository;
    private final UserAlertRepository userAlertRepository;
    private final UserTenantRepository userTenantRepository;
    private final ConcurrentMap<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    @Transactional(readOnly = true)
    public DashboardOverviewDto getOverview(AuthContext.TenantSession session) {
        UUID userId = session.userId();
        UUID tenantId = session.tenantId();
        validateUserTenant(userId, tenantId);

        long totalDevices = userDeviceRepository.countByUserUserIdAndDeviceTenantTenantId(userId, tenantId);
        long onlineDevices = userDeviceRepository
                .countByUserUserIdAndDeviceStatusAndDeviceTenantTenantId(userId, STATUS_ONLINE, tenantId);
        long alertCount = userAlertRepository.countByUserUserIdAndAlertTenantTenantId(userId, tenantId);
        UserAlert lastAlert = userAlertRepository
                .findFirstByUserUserIdAndAlertTenantTenantIdOrderByNotifiedAtDesc(userId, tenantId)
                .orElse(null);

        return new DashboardOverviewDto(
                (int) totalDevices,
                (int) onlineDevices,
                alertCount,
                lastAlert != null && lastAlert.getNotifiedAt() != null
                        ? lastAlert.getNotifiedAt().toString()
                        : null
        );
    }

    @Transactional(readOnly = true)
    public DashboardDeviceListResponseDto getDevices(AuthContext.TenantSession session) {
        UUID userId = session.userId();
        UUID tenantId = session.tenantId();
        validateUserTenant(userId, tenantId);
        List<DashboardDeviceDto> devices = userDeviceRepository
                .findAllByUserUserIdAndDeviceTenantTenantId(userId, tenantId)
                .stream()
                .map(DashboardDeviceDto::from)
                .toList();
        return new DashboardDeviceListResponseDto(devices);
    }

    @Transactional(readOnly = true)
    public RecentAlertListResponseDto getRecentAlerts(AuthContext.TenantSession session, Integer limit) {
        UUID userId = session.userId();
        UUID tenantId = session.tenantId();
        validateUserTenant(userId, tenantId);
        int size = limit == null || limit <= 0 ? 10 : limit;
        List<AlertResponseDto> alerts = userAlertRepository
                .findByUserUserIdAndAlertTenantTenantIdOrderByNotifiedAtDesc(userId, tenantId)
                .stream()
                .limit(size)
                .map(AlertResponseDto::from)
                .toList();
        return new RecentAlertListResponseDto(alerts);
    }

    public SseEmitter stream(AuthContext.TenantSession session) {
        UUID userId = session.userId();
        UUID tenantId = session.tenantId();
        validateUserTenant(userId, tenantId);
        SseEmitter emitter = new SseEmitter(STREAM_TIMEOUT_MILLIS);
        String key = emitterKey(userId, tenantId);
        emitters.compute(key, (k, list) -> {
            List<SseEmitter> updated = list != null ? list : new CopyOnWriteArrayList<>();
            updated.add(emitter);
            return updated;
        });

        Runnable removeCallback = () -> removeEmitter(key, emitter);
        emitter.onCompletion(removeCallback);
        emitter.onTimeout(removeCallback);
        emitter.onError(ex -> removeCallback.run());

        return emitter;
    }

    public void sendAlertToUser(UserAlert alert) {
        if (alert.getUser() == null || alert.getUser().getUserId() == null) {
            return;
        }
        UUID tenantId = alert.getAlert() != null && alert.getAlert().getTenant() != null
                ? alert.getAlert().getTenant().getTenantId()
                : null;
        if (tenantId == null) {
            return;
        }
        alert.ensureTenant(tenantId);
        alert.ensureUser(alert.getUser().getUserId());
        String key = emitterKey(alert.getUser().getUserId(), tenantId);
        List<SseEmitter> emitterList = emitters.get(key);
        if (emitterList == null || emitterList.isEmpty()) {
            return;
        }

        emitterList.forEach(emitter -> {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("alert")
                                .data(AlertResponseDto.from(alert))
                );
            } catch (Exception ex) {
                removeEmitter(key, emitter);
                emitter.completeWithError(ex);
            }
        });
    }

    private void validateUserTenant(UUID userId, UUID tenantId) {
        userTenantRepository.validateUserBelongsToTenant(userId, tenantId);
    }

    private String emitterKey(UUID userId, UUID tenantId) {
        return userId + ":" + tenantId;
    }

    private void removeEmitter(String key, SseEmitter emitter) {
        emitters.computeIfPresent(key, (k, list) -> {
            list.remove(emitter);
            return list.isEmpty() ? null : list;
        });
    }
}
