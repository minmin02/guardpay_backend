package com.example.guardpay.domain.assessment.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Part {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer partId;

    @Column(nullable = false, length = 50)
    private String partName; // 파트이름

    @Column(nullable = false, length = 250)
    private String partDescription; // 파트설명

}
