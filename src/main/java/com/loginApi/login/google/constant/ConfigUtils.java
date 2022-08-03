package com.loginApi.login.google.constant;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Getter
public class ConfigUtils {

//    @Value("${google.auth.url}")
    private String googleAuthUrl = "https://oauth2.googleapis.com";

//    @Value("${google.login.url}")
    private String googleLoginUrl = "https://accounts.google.com";

//    @Value("${google.redirect.uri}")
    private String googleRedirectUrl = "http://localhost:8080/google/login/redirect";

//    @Value("${google.client.id}")
    private String googleClientId = "317022284825-dao0e67kivhlkrtihcm637fj8cpcsa6g.apps.googleusercontent.com";

//    @Value("${google.secret}")
    private String googleSecret = "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";

//    @Value("${google.auth.scope}")
    private String scopes = "profile,email,openid";

    public String googleInitUrl(){
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getGoogleClientId());
        params.put("redirect_uri", getGoogleRedirectUrl());
        params.put("response_type", "code");
        params.put("scope", getScopeUrl());

        String paramStr = params.entrySet().stream().map(o -> o.getKey() + "=" + o.getValue()).collect(Collectors.joining("&"));

        return getGoogleLoginUrl() + "/o/oauth2/v2/auth" + "?" + paramStr;
    }

    public String getScopeUrl(){
        return scopes.replace(",", "%20");
    }
}
