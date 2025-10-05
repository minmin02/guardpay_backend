package com.example.guardpay.domain.member.controller;


import com.example.guardpay.domain.member.dto.SignupRequestDto;
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
@RequestMapping("/api/users")
public class MemberApiConroller {

    private final MemberService memberService; // 3. final 키워드 추가

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
}