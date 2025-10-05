package com.example.guardpay.domain.member.repository;


import com.example.guardpay.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
