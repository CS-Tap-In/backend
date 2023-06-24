package com.cstapin.auth.config;

import com.cstapin.auth.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제
        http.csrf().disable(); // postman 접근해야 함!! - CSR 할때!!

        // 2. iframe 거부
        http.headers().frameOptions().disable();

        // 3. cors 재설정
        http.cors().configurationSource(configurationSource());

        // 4. jSessionId 사용 거부
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 5. form 로긴 해제 (UsernamePasswordAuthenticationFilter 비활성화)
        http.formLogin().disable();

        // 6. 로그인 인증창이 뜨지 않게 비활성화
        http.httpBasic().disable();

        // 7. 커스텀 필터 적용 (시큐리티 필터 교환)
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        // 8. 인증, 권한 필터 설정
        http.authorizeRequests(
                authorize -> authorize
                        .antMatchers("/api/v1/user/**")
                        .hasAuthority("USER")
                        .antMatchers("/api/v1/admin/**")
                        .hasAuthority("ADMIN")
                        .anyRequest().permitAll()

        );

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}