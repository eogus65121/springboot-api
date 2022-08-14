package com.loginApi.login.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KaKaoLoginStatRes {
    private String id;
    private String expiresInMillis;
    private String expires_in;
    private String app_id;
}
