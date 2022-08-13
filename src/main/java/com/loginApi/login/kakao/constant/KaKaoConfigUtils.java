package com.loginApi.login.kakao.constant;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class KaKaoConfigUtils {

    private String kakaoClientId = "2f4e81007fe7b19f83060d1b69eab93e";

    private String kakaoRedirectUrl = "http://localhost:8080/kakao/login/redirect";

    private String kakaoAuthUrl = "https://kauth.kakao.com/oauth/token";

    private String kakaoLoginUrl = "";

    private String kakaoLoginCheckUrl = "https://kapi.kakao.com/v1/user/access_token_info";

    private String kakaoSecret = "iTYu7uQeWDmun0Ib5LJNimSIJiVa20Xa";

    public String kakaoInitUrl(){
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getKakaoClientId());
        params.put("redirect_uri", getKakaoRedirectUrl());
        params.put("response_type", "code");
        String paramStr = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + params.get("client_id")
                + "&redirect_uri=" + params.get("redirect_uri") + "&response_type=" +params.get("response_type");
        return paramStr;
    }
}
