package com.example.guardpay.domain.member.service;

import com.example.guardpay.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class checkEmailService {

    private final MemberRepository memberRepository;

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
