package com.example.demo.dto.alert;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AckRequestDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String tenantId;

    public AckRequestDto(String userId, String tenantId) {
        this.userId = userId;
        this.tenantId = tenantId;
    }
}
