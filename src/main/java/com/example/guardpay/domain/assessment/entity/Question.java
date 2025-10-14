package com.example.guardpay.domain.assessment.entity;


import com.example.guardpay.domain.assessment.dto.OptionDto;
import com.example.guardpay.domain.assessment.dto.converter.OptionsJsonConverter;
import com.example.guardpay.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer questionId;

    @Column(nullable = false, length = 245)
    private String questionText; // 문제내용

    //외부 파일(CSV, Excel, JSON) 읽기 진단 데이터를 json 컨버터 이용해서 매핑 시켜야 할듯?
    // options 필드를 DB에 저장할 때 OptionsJsonConverter를 사용
    @Convert(converter = OptionsJsonConverter.class)
    @Column(name = "options", columnDefinition = "JSON") // DB 컬럼 타입을 명시
    private List<OptionDto> options;


    @Column(name = "correct_answer", nullable = false)
    private Integer correctAnswer; //정답 번호

    @ManyToOne(fetch = FetchType.LAZY)//파트  N:1관계매핑
    @JoinColumn(name="part_id")
    private Part part;



}
