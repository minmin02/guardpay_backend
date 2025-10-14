package com.example.guardpay.domain.assessment.entity;


import com.example.guardpay.domain.member.data.Grade;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

//회원 진단이력 
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DiagnosisHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer diagnosisHistoryId;

    @Column(nullable = false)
    private Integer score; //총 점수

    @Enumerated(EnumType.STRING) //최종 등급
    @Column(name = "final_grade", nullable = false, length = 20)
    private Grade finalGrade;

    @ManyToOne(fetch = FetchType.LAZY)//질문테이블  N:1관계매핑
    @JoinColumn(name="member_id")
    private Member member;

}
