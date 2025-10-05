package com.example.guardpay.domain.bank.entity;


import com.example.guardpay.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

// 회원 보유 계좌
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Accounts {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer accountId;
    
    @Column(nullable = false)
    private String bankName; // 은행이름

    @Column(nullable = false)
    private String accountNumber; // 계좌번호

    @Column(nullable = false)
    private String name; // 은행이름

    @Column(nullable = false)
    private String balance; // 잔액

    @Column(nullable = false)
    private Boolean isSelected; // 사용여부

    @ManyToOne(fetch = FetchType.LAZY)//은행 테이블과 N:1관계매핑
    @JoinColumn(name="bank_id")
    private Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)//은행 테이블과 N:1관계매핑
    @JoinColumn(name="member_id")
    private Member member;



}
