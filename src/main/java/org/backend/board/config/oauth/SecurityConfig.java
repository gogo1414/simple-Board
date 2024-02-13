package org.backend.board.config.oauth;

import lombok.RequiredArgsConstructor;
import org.backend.board.config.oauth.CustomOAuth2UserService;
import org.backend.board.domain.member.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // Spring Security 설정 활성화
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private static final String[] NON_AUTH_LIST = {
            "/",
            "/css/**",
            "/images/**",
            "/js/**",
            "/swagger-ui/**",
            "/h2-console/**"
    };

    private static final String[] AUTH_LIST = {
            "/api/v1/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) // csrf 보안 사용 X

                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))  //보안 헤더 설정

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(NON_AUTH_LIST).permitAll() // 기본 접근 가능
                        .requestMatchers(AUTH_LIST).hasRole(Role.MEMBER.name()) // Role User여야 가능
                        .anyRequest().authenticated()) // 나머지 경로는 모두 인증된 사용자만 접근 가능
                .logout(logout -> logout.logoutSuccessUrl("/"))

                .oauth2Login(oauth2Login -> oauth2Login
                .userInfoEndpoint(UserInfoEndpointConfig -> UserInfoEndpointConfig
                        .userService(customOAuth2UserService)));

        return http.build();
    }
}