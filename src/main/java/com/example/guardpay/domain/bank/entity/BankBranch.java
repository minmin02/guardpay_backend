package com.example.guardpay.domain.bank.entity;

import jakarta.persistence.*;
import lombok.*;


//은행 지점
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BankBranch {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer branchId;

    @Column(nullable = false, length = 50)
    private String name; // 은행이름

    @ManyToOne(fetch = FetchType.LAZY)//은행 테이블과 N:1관계매핑
    @JoinColumn(name="bank_id")
    private Bank bank;


}
