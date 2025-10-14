package com.example.guardpay.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    // 1. application.yml에서 설정값 주입받기
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    private Key key;

    // 2. 주입받은 secretKey를 기반으로 암호화 키(Key) 객체 생성
    @PostConstruct
    protected void init() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 3. AccessToken 생성 로직
    public String createAccessToken(Integer memberId) {
        return createToken(String.valueOf(memberId), accessTokenValidityInSeconds);
    }

    // 4. RefreshToken 생성 로직
    public String createRefreshToken(Integer memberId) {
        return createToken(String.valueOf(memberId), refreshTokenValidityInSeconds);
    }

    // 5. 토큰 생성 공통 로직
    private String createToken(String subject, long validityInSeconds) {
        Claims claims = Jwts.claims().setSubject(subject);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(validity) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘과 signature
                .compact();
    }

    // --- 아래부터는 토큰 검증 및 정보 추출에 필요한 메소드들 ---

    // 6. 토큰에서 회원 ID 추출
    public Integer getMemberIdFromToken(String token) {
        // 토큰의 payload에서 subject(회원 ID)를 추출
        String memberId = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Integer.parseInt(memberId);
    }

    // 7. 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
}