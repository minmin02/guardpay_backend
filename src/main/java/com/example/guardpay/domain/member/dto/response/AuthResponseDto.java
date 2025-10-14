package com.example.guardpay.domain.member.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private boolean isNewuser;// 신규유저인지 파악
}
