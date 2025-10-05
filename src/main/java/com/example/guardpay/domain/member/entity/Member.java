package com.example.guardpay.domain.member.entity;

import com.example.guardpay.domain.member.data.Grade;
import com.example.guardpay.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

@Id
@GeneratedValue(strategy= GenerationType.IDENTITY)
private Integer memberId;


    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false, length = 50)
    private String nickname; // 닉네임


    private int points; // 포인트

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(length = 20)
    private String status; // 회원상태

    private int exp; // 경험치

    @Column(name = "font_size")
    private int fontSize; // 폰트크기

    @CreatedDate // 엔터티 생성 시 시간 자동 저장
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 회원가입일시

    @LastModifiedDate // 엔터티 수정 시 시간 자동 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 회원정보수정일시

    @Column(length = 20)
    private String provider; // 소셜 로그인 제공자

    @Column(name = "provider_id")
    private String providerId; // 소셜ID

    @Column(length = 20)
    private String role; // 사용자 권한

    @Builder
    public Member(String email, String password, String nickname, String role, String provider, String providerId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.points = 0;
        this.grade = Grade.BRONZE;
        this.status = "ACTIVE"; // 예: 활성 상태를 기본값으로 지정
        this.exp = 0;
        this.fontSize = 16; // 예: 기본 폰트 크기
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

}
