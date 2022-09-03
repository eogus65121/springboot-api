package com.loginApi.login.kakao.constant;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class KaKaoConfigUtils {

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.url}")
    private String kakaoRedirectUrl;

    @Value("${kakao.auth.url}")
    private String kakaoAuthUrl;

    @Value("${kakao.login.url}")
    private String kakaoLoginUrl;

    @Value("${kakao.login.check.url}")
    private String kakaoLoginCheckUrl;

    @Value("${kakao.secret}")
    private String kakaoSecret;

    public String kakaoInitUrl(){
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getKakaoClientId());
        params.put("redirect_uri", getKakaoRedirectUrl());
        params.put("response_type", "code");
        String paramStr = kakaoLoginUrl + params.get("client_id")
                + "&redirect_uri=" + params.get("redirect_uri") + "&response_type=" +params.get("response_type");
        return paramStr;
    }
}
