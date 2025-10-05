package com.example.guardpay.domain.video.entity;


import com.example.guardpay.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


//영상 카테고리
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VideoCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(nullable = false, length = 50)
    private String categoryName;

    @Column(nullable = false, length = 250)
    private String content;



}
