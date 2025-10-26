package com.example.guardpay.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {

    private String accessToken;
    private String refreshToken;

}
