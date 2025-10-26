package com.example.guardpay.global.jwt;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.global.exception.MemberNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {

    private final MemberRepository memberRepository;

    private Key key;
    private final String secretKey;
    private final long accessTokenValidityInMs;
    private final long refreshTokenValidityInMs;

    public static final String CLAIM_TYPE = "type";   // "access" | "refresh"
    public static final String CLAIM_ROLE = "role";   // "USER" 등

    public JwtTokenProvider(
            MemberRepository memberRepository,
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds
    ) {
        this.memberRepository = memberRepository;
        this.secretKey = secretKey;
        this.accessTokenValidityInMs = accessTokenValidityInSeconds * 1000L;
        this.refreshTokenValidityInMs = refreshTokenValidityInSeconds * 1000L;
    }

    @PostConstruct
    void init() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /** AccessToken: sub=memberId, claims: {type=access, role=...} */
    public String createAccessToken(Integer memberId, String role) {
        //memberId 문자열로 바꿈 subject로 넘김
        return createToken(String.valueOf(memberId), accessTokenValidityInMs,
                Map.of(CLAIM_TYPE, "access", CLAIM_ROLE, role));
    }

    public String createRefreshToken(Integer memberId) {
        return createToken(String.valueOf(memberId), refreshTokenValidityInMs,
                Map.of(CLAIM_TYPE, "refresh"));
    }


    private String createToken(String subject, long validityInMs, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityInMs);

        Claims claims = Jwts.claims().setSubject(subject);
        if (extraClaims != null && !extraClaims.isEmpty()) {
            claims.putAll(extraClaims);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** 토큰 검증 (서명/만료) */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.warn("유효하지 않은 토큰: {}", e.getMessage());
            return false;
        }
    }

    /** 토큰에서 memberId(sub) 추출 */
    public Long getMemberId(String token) {
        String sub = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.valueOf(sub); // Integer를 String으로 넣었지만 Long으로 꺼내도 무방
    }


    /** 토큰에서 type(access|refresh) 추출 (옵션) */
    public String getTokenType(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get(CLAIM_TYPE);
    }

    /** 토큰에서 role 추출 (옵션) */
    public String getRole(String token) {
        Object r = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get(CLAIM_ROLE);
        return r == null ? null : String.valueOf(r);
    }

    /**
     * Authentication 생성:
     *  - 토큰 서명/만료 검증 후, sub(memberId)로 DB 조회하여 최신 권한/상태 반영
     */
    public Authentication getAuthentication(String token) {
        Long memberId = getMemberId(token);

        Member m = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 최신 권한 반영 (예: m.getRole().name() -> "USER")
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + m.getRole()));

        // principal은 보통 고유키를 username으로 둡니다.
        var principal = new org.springframework.security.core.userdetails.User(
                String.valueOf(m.getMemberId()), "", authorities);


        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

}
