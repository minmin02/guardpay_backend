package com.example.guardpay.domain.member.controller;


import com.example.guardpay.domain.member.dto.request.KakaoToken;
import com.example.guardpay.domain.member.dto.request.SignupRequestDto;
import com.example.guardpay.domain.member.dto.response.AuthResponseDto;
import com.example.guardpay.domain.member.service.AuthService;
import com.example.guardpay.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberApiConroller {

    private final MemberService memberService; // 3. final 키워드 추가
    private final AuthService kakaoAuthService; // 신규유저, 기존 유저확인 로직(카카오)


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
}
