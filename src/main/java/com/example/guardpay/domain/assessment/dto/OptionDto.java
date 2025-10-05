package com.example.guardpay.domain.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionDto {
    private int no; // 선택지 번호
    private String content; // 선택지 내용
}
