package com.example.demo.controller;

import com.example.demo.dto.ApiResponseDto;
import com.example.demo.dto.SimpleMessageResponseDto;
import com.example.demo.dto.user.LoginRequestDto;
import com.example.demo.dto.user.LogoutRequestDto;
import com.example.demo.dto.user.SignUpRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "User Auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> signup(@Valid @RequestBody SignUpRequestDto req) {
        UserResponseDto data = authService.signup(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.ok(data));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> login(@Valid @RequestBody LoginRequestDto req) {
        UserResponseDto data = authService.login(req);
        return ResponseEntity.ok(ApiResponseDto.ok(data));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<SimpleMessageResponseDto>> logout(
            @Valid @RequestBody(required = false) LogoutRequestDto request
    ) {
        if (request != null && request.getUserId() != null) {
            authService.logout(request.getUserId());
        }
        SimpleMessageResponseDto response = SimpleMessageResponseDto.of("logout success");
        return ResponseEntity.ok(ApiResponseDto.ok(response));
    }
}
