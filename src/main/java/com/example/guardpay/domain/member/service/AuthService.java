package com.example.guardpay.domain.member.service;

import com.example.guardpay.domain.member.dto.response.AuthResponseDto;
import com.example.guardpay.domain.member.dto.response.KakaoUserInfo;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpHeaders; // <- 올바른 import
import java.util.Optional;

//소셜 (카카오)
@Service
@RequiredArgsConstructor
@Transactional // DB 작업을 하므로 트랜잭션 처리
@Slf4j // 👈 2. Lombok의 Slf4j 어노테이션 추가
public class AuthService {

    private final MemberRepository memberRepository; // UserRepository -> MemberRepository
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDto loginOrSignup(String kakaoAccessToken) {


        log.info("➡️ Received request for Kakao login/signup.");
        log.debug("Received Kakao Access Token: {}", kakaoAccessToken);
        // 1. 카카오 서버에서 사용자 정보 가져오기
        log.info("... Requesting user info from Kakao API ...");

        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);
        Long kakaoId = kakaoUserInfo.getId();

        // 2. DB에서 카카오 ID로 사용자 조회
        Optional<Member> optionalMember = memberRepository.findByProviderId(String.valueOf(kakaoId)); // User -> Member

        Member member; // User -> Member
        boolean isNewUser;

        if (optionalMember.isPresent()) {
            // 3-1. 사용자가 존재하면 (기존 회원 -> 로그인)
            member = optionalMember.get();
            log.info("👤 Existing member found. Member ID: {}", member.getMemberId());

            isNewUser = false;
        } else {
            // 3-2. 사용자가 없으면 (신규 회원 -> 자동 회원가입)
            String nickname = kakaoUserInfo.getKakao_account().getProfile().getNickname();
            String email = kakaoUserInfo.getKakao_account().getEmail();

// Member 엔티티에 만들어 둔 정적 팩토리 메소드를 사용합니다.
            Member newMember = Member.createSocialMember(
                    email,
                    nickname,
                    "kakao", // provider는 "kakao"로 지정
                    String.valueOf(kakaoId) // providerId는 Long 타입이므로 String으로 변환
            );
            log.info("✨ New member created. Member ID: {}", newMember.getMemberId());

            member = memberRepository.save(newMember);

            isNewUser = true;
        }

        // 4. 우리 서비스의 JWT 토큰 발급 (Member의 ID 사용)
        String serviceAccessToken = jwtTokenProvider.createAccessToken(member.getMemberId()); // ✅ OK: public 메소드 호출

        // 5. 최종 응답 DTO 생성 후 반환
        return new AuthResponseDto(serviceAccessToken, isNewUser);
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        // 1. HTTP 요청을 보내기 위한 RestTemplate 객체 생성
        RestTemplate rt = new RestTemplate();

        // 2. HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 3. HTTP 헤더를 포함한 요청 객체 생성
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            // 4. 카카오 사용자 정보 API로 POST 요청 보내기
            //    - 요청 URL: https://kapi.kakao.com/v2/user/me
            //    - 응답은 KakaoUserInfo 클래스로 자동 매핑(역직렬화)됨
            ResponseEntity<KakaoUserInfo> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    request,
                    KakaoUserInfo.class
            );

            // 5. 응답 본문(사용자 정보) 반환
            return response.getBody();

        } catch (HttpClientErrorException e) {
            // 6. 예외 처리 (예: 401 Unauthorized - 유효하지 않은 토큰)
            // 클라이언트에게 유효하지 않은 토큰임을 알리는 예외를 던짐
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 카카오 액세스 토큰입니다.");
        }
    }

}
