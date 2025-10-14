package com.example.guardpay.domain.member.service;


import com.example.guardpay.domain.member.dto.request.SignupRequestDto;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    // Spring Security의 PasswordEncoder를 주입받아야 하지만, 우선 개념 설명을 위해 로직만 구현합니다.
    // private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignupRequestDto requestDto) {
        // 비밀번호 암호화 로직 (실제로는 PasswordEncoder 사용)
        // String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(requestDto.getPassword()) // 암호화된 비밀번호를 저장해야 합니다.
                .nickname(requestDto.getNickname())
                .build();

        memberRepository.save(member);
    }
}