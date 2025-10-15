package com.example.guardpay.global.config;

import com.example.guardpay.global.auth.CustomOAuth2UserService;
import com.example.guardpay.global.auth.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import lombok.RequiredArgsConstructor;

// ⚠️ 아래 두 클래스는 직접 생성해야 합니다. (이전 답변 참고)
// import com.yourpackage.security.oauth.CustomOAuth2UserService;
// import com.yourpackage.security.oauth.OAuth2AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // final 필드를 주입받기 위해 추가
public class SecurityConfig {

    // ✅ final로 두 핸들러/서비스를 선언
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // ✅ 1. 기존 csrf 비활성화는 그대로 사용
        http.csrf(csrf -> csrf.disable());

        // ✅ 2. 불필요한 인증 방식 비활성화
        http
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
                .formLogin(formLogin -> formLogin.disable()); // 기본 폼 로그인 비활성화

        // ✅ 3. 세션 정책을 STATELESS로 설정 (JWT 사용을 위함)
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // ✅ 4. 경로별 권한 설정
        http
                .authorizeHttpRequests(auth -> auth
                        // 로그인/회원가입 관련 경로는 누구나 접근 가능하도록 허용
                        .requestMatchers("/api/auth/**", "/oauth2/**").permitAll()
                        // 그 외 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                );

        // ✅ 5. 가장 중요한 OAuth2 로그인 설정 (시큐리티 컨트롤러에서 소셜 로그인 api 가로채서 로직수행)
        http
                .oauth2Login(oauth2 -> oauth2
                        // 로그인 성공 후 처리를 담당할 핸들러 등록
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        // 인증된 사용자 정보를 처리할 서비스 등록 (인증하는 단계)
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService)
                        )
                );

        return http.build();
    }
}