package com.example.demo.dto.device;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeviceRegisterRequestDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String tenantId;

    @NotBlank
    private String vendor;

    @NotBlank
    private String model;

    @NotBlank
    private String macAddr;

    @NotBlank
    private String category;

    public DeviceRegisterRequestDto(
            String userId,
            String tenantId,
            String vendor,
            String model,
            String macAddr,
            String category
    ) {
        this.userId = userId;
        this.tenantId = tenantId;
        this.vendor = vendor;
        this.model = model;
        this.macAddr = macAddr;
        this.category = category;
    }
}
