package com.wanted.preonboarding.backend.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JWTDTO {
    String accessToken;
    String refreshToken;
}
