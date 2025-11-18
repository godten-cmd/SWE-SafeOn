package com.example.demo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutRequestDto {

    @NotBlank
    private String userId;

    public LogoutRequestDto(String userId) {
        this.userId = userId;
    }
}
