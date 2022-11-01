package com.loginApi.login.naver.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NaverConfigUtils{

    @Value("${naver.auth.url}")
    private String naverAuthUrl;

    @Value("${naver.token.url}")
    private String naverTokenUrl;

    @Value("${naver.req.profile.url")
    private String naverProfileUrl;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.secret}")
    private String secret;

    @Value("${naver.state.info}")
    private String statInfo;

    @Value("${naver.redirect.url}")
    private String callbackUrl;
}