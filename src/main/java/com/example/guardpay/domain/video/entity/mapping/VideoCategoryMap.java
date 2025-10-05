package com.example.guardpay.domain.video.entity.mapping;

import com.example.guardpay.domain.member.entity.TERM;
import com.example.guardpay.domain.video.entity.Video;
import com.example.guardpay.domain.video.entity.VideoCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VideoCategoryMap {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="video_category_id") // ✅ 2. DB 컬럼 이름은 그대로 유지
    private VideoCategory videoCategory;


    @ManyToOne(fetch = FetchType.LAZY)//비디오 테이블과 N:1관계매핑
    @JoinColumn(name="video_id")
    private Video video;



}
