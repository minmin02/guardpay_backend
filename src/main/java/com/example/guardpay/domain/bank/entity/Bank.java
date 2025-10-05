package com.example.guardpay.domain.bank.entity;

import jakarta.persistence.*;
import lombok.*;

//은행
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Bank {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer bankId;


    @Column(nullable = false)
    private String name; // 은행이름

    @Column(nullable = false)
    private String code; // 은행코드


}
