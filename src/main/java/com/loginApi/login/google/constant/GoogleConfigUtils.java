package com.loginApi.login.google.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Getter
public class GoogleConfigUtils {

    @Value("${google.auth.url}")
    private String googleAuthUrl;

    @Value("${google.login.url}")
    private String googleLoginUrl;

    @Value("${google.redirect.uri}")
    private String googleRedirectUrl;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.secret}")
    private String googleSecret;

    @Value("${google.auth.scope}")
    private String scopes;

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
