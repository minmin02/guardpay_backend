package com.example.guardpay.domain.bank.entity;

import com.example.guardpay.domain.bank.entity.data.TransferStatus;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Transfers extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer transferId;

    @Column(nullable = false, length = 50)
    private String inputbankName; // 송금은행이름

    @Column(nullable = false, length = 245)
    private String inputAccountNumber; // 은행 로고url

    @Column(nullable = false, length = 50)
    private String amount; // 금액

    @Column(nullable = false, length = 245)
    private String message; // 송금 메시지?

    @Column(nullable = false)
    private Boolean isSelected; // 사용여부

    @Enumerated(EnumType.STRING) // Enum 문자열 그대로 DB에 저장
    @Column(name = "status", nullable = false)
    private TransferStatus status;

    @ManyToOne(fetch = FetchType.LAZY)//회원  테이블과 N:1관계매핑
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)//회원 보유 계좌 테이블과 N:1관계매핑
    @JoinColumn(name="account_id")
    private Accounts account;

    @ManyToOne(fetch = FetchType.LAZY)//가상계좌테이블과 N:1관계매핑
    @JoinColumn(name="virtualUsers_id")
    private VirtualUsers virtualUsers;

}
