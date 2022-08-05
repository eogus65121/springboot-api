package com.loginApi.login.kakao.constant;

import lombok.Getter;

@Getter
public class KaKaoConfigUtils {

    private String kakaoClientId = "2f4e81007fe7b19f83060d1b69eab93e";

    private String kakaoRedirectUrl = "http://localhost:8080/kakao/login/redirect";

    private String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize";

    public String kakaoInitUrl(){
        String paramStr = "?response_type=code&client_id=" + getKakaoClientId() + "&redirect_uri=" + getKakaoRedirectUrl();
        return paramStr;
    }
}
