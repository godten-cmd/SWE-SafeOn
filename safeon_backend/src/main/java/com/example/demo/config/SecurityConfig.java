package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // REST 개발 초기에 편의상 비활성화
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()   // 전체 허용
                )
                .formLogin(login -> login.disable()) // 기본 로그인 폼 비활성화
                .httpBasic(basic -> basic.disable()); // 기본 인증 비활성화(선택)
        return http.build();
    }
}
