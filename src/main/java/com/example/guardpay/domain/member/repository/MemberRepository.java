package com.example.guardpay.domain.member.repository;


import com.example.guardpay.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //소셜 로그인 providerId로 회원을 찾는 메서드

    Optional<Member> findByProviderId(String providerId);
    Optional<Member> findByEmail(String email);


}
