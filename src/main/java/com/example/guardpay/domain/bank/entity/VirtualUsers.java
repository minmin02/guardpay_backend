package com.example.guardpay.domain.bank.entity;


import jakarta.persistence.*;
import lombok.*;

//가상계좌
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VirtualUsers {



    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer virtualUserId;


    @Column(nullable = false)
    private String name; // 사용자이름

    @Column(nullable = false, length = 50)
    private String bankName; // 사용자이름

    @Column(nullable = false, length = 50)
    private String accountNumber; // 계좌번호

    @Column(nullable = false, length = 255)
    private String profileUrl; // 프로필 url

    @ManyToOne(fetch = FetchType.LAZY)//은행 테이블과 N:1관계매핑
    @JoinColumn(name="bank_id")
    private Bank bank;

}
