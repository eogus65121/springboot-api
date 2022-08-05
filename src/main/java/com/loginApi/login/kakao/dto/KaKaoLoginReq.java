package com.loginApi.login.kakao.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@Builder
public class KaKaoLoginReq {

    // 토큰 req
    @NotNull
    private String grant_type;          //authorization_code
    @NotNull
    private String client_id;           //app REST_API key
    @NotNull
    private String redirect_uri;        //redirect_uri
    @NotNull
    private String code;                //인가 코드 받기 요청으로 얻은 인가 코드
    private String client_secret;       // 토큰 발급 시 보안 강화를 위한 추가 확인 코드 : reqx


}
