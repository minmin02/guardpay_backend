package com.example.guardpay.domain.member.controller;


import com.example.guardpay.domain.member.dto.request.KakaoToken;
import com.example.guardpay.domain.member.dto.request.LoginRequest;
import com.example.guardpay.domain.member.dto.request.SignupRequestDto;
import com.example.guardpay.domain.member.dto.response.AuthResponseDto;
import com.example.guardpay.domain.member.dto.response.JwtResponse;
import com.example.guardpay.domain.member.service.AuthService;
import com.example.guardpay.domain.member.service.MemberLoginService;
import com.example.guardpay.domain.member.service.MemberSignService;
import com.example.guardpay.domain.member.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberApiController {

    private final MemberSignService memberService; // 3. final 키워드 추가

    private final MemberLoginService memberLoginService;
    private final AuthService kakaoAuthService; // 신규유저, 기존 유저확인 로직(카카오)

    private final PasswordResetService passwordResetService; // ⬅️ 새로 주입

    //폼 회원가입
    @PostMapping("/signup")
    // 4. 반환 타입을 Map<String, Object>로 수정
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody SignupRequestDto signUpRequestDto) {

        // 서비스 계층을 호출하여 회원가입 로직 수행
        memberService.signUp(signUpRequestDto);

        // 5. 성공 시 Map을 사용해 체계적인 JSON 응답 반환
        Map<String, Object> response = new HashMap<>();

        response.put("status", 201);
        response.put("message", "회원가입이 성공적으로 완료되었습니다.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//카카오 회원 가입
    @PostMapping("/kakao")
    public ResponseEntity<AuthResponseDto> kakaoAuth(@RequestBody KakaoToken kakaoTokenDto) {
        // Service 로직을 호출하고 결과를 바로 반환
        AuthResponseDto responseDto = kakaoAuthService.loginOrSignup(kakaoTokenDto.getAccessToken());
        return ResponseEntity.ok(responseDto);
    }

    // 폼 로그인

    @PostMapping("/login") // ⬅️ @GetMapping이 아닌 @PostMapping
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {

        // @RequestBody:
        // 클라이언트가 보낸 JSON 형식의 본문을 LoginRequest 객체로 변환해줍니다.

        // 1. 서비스 호출
        JwtResponse jwtResponse = memberLoginService.login(loginRequest);
        // 2. 응답 반환
        // HTTP 200 OK 상태 코드와 함께 JWT 토큰이 담긴 본문을 반환합니다.
        return ResponseEntity.ok(jwtResponse);
    }

    //임시 비번 메일 발송
    @PostMapping("/password-reset-request")
    public ResponseEntity<Map<String, Object>> requestPasswordReset(
            @RequestBody Map<String, String> requestBody) {

        String email = requestBody.get("email");

        // 2. 서비스 로직 호출
        passwordResetService.issueTemporaryPassword(email);

        // 3. (보안상) 유저 존재 여부와 상관없이 항상 동일한 성공 응답을 보냄
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "비밀번호 재설정 메일이 발송되었습니다. 메일함을 확인해주세요.");

        return ResponseEntity.ok(response);
    }




}
