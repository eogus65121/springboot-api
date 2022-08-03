package com.loginApi.login.google.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@NoArgsConstructor
public class GoogleLoginRes {
    private String accessToken;
    private String expiresIn;   // access token의 남은 수명
    private String refreshToken;    // 새 엑세스 토큰을 얻는데 사용할 수 있는 토큰
    private String scope;
    private String tokenType;   // 토큰 유형(Bearer 고정)
    private String idToken;
}
