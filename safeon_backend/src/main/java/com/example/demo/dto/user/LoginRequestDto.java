package com.example.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
