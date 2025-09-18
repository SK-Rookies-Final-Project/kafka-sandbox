package com.sk_rookies.kafkasandbox.config;

// src/main/java/.../SecurityConfig.java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())   // 🔸 CORS 활성화 (WebConfig와 연동)
                .csrf(csrf -> csrf.disable())      // 필요시 비활성(개발용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll() // 🔸 Preflight 허용
                        .requestMatchers("/api/**").permitAll() // 필요 정책에 맞게 조정
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
