package com.example.guardpay.domain.member.service;

import com.example.guardpay.domain.member.dto.request.LoginRequest;
import com.example.guardpay.domain.member.dto.response.JwtResponse;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.global.exception.InvalidPasswordException;
import com.example.guardpay.global.exception.MemberNotFoundException;
import com.example.guardpay.global.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MemberLoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public JwtResponse login(LoginRequest loginRequest) {

        
        //이메일로 회원 객체 뽑아옴
        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);  

        //비밀번호 검증
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new InvalidPasswordException();
        }


        String accessToken  = jwtTokenProvider.createAccessToken(member.getMemberId(), member.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getMemberId());
        return new JwtResponse(accessToken, refreshToken);

    }
}
