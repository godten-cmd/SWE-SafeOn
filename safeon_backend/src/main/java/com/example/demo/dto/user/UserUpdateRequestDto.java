package com.example.demo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String tenantId;

    private String name;
    private String password;

    public UserUpdateRequestDto(String userId, String tenantId, String name, String password) {
        this.userId = userId;
        this.tenantId = tenantId;
        this.name = name;
        this.password = password;
    }
}
