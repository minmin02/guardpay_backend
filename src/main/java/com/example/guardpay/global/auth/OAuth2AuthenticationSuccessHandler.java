package com.example.guardpay.global.auth;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository; // ✅ DB 조회를 위해 MemberRepository 주입

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // 1. Authentication 객체에서 OAuth2User 정보를 가져옵니다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 2. OAuth2User에서 이메일을 추출합니다.
        String email = oAuth2User.getAttribute("email");

        // 3. 이메일을 사용해 DB에서 Member를 조회하고 memberId를 가져옵니다.
        //    orElseThrow를 사용하여 해당 이메일의 유저가 없으면 예외를 발생시킵니다.
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Integer memberId = member.getMemberId();
        log.info("OAuth2 로그인 성공! Member ID: {}", memberId);

        // 4. 조회한 memberId(Integer)를 사용해 JWT를 생성합니다.
        String accessToken = jwtTokenProvider.createAccessToken(memberId);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);

        // 5. 토큰을 담아 프론트엔드로 리디렉션합니다.
        String targetUrl = createRedirectUrl(accessToken, refreshToken);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String createRedirectUrl(String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString("guardpay://oauth-redirect")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
    }
}