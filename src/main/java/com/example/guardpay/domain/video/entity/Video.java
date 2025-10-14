package com.example.guardpay.domain.video.entity;

import com.example.guardpay.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Video extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer videoId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 250)
    private String description;

    @Column(nullable = false, length = 250)
    private String url;

    @Column(nullable = false, length = 250)
    private String thumbnailUrl;

    @Column(nullable = false, length = 50)
    private String source; //제공기관






}
