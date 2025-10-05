package com.example.guardpay.domain.assessment.entity;

import com.example.guardpay.domain.bank.entity.VirtualUsers;
import jakarta.persistence.*;
import lombok.*;

//회원 문제 기록
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HistoryDetail {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer historyDetailId; //pk

    @Column(nullable = false)
    private Boolean isCorrect; // 정답여부

    @Column(nullable = false)
    private Integer selectedAnswer; //정답번호

    @ManyToOne(fetch = FetchType.LAZY)//질문테이블  N:1관계매핑
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)//회원진단이력  N:1관계매핑
    @JoinColumn(name="diagonosisHistory_id")
    private DiagnosisHistory diagnosisHistory;

}
