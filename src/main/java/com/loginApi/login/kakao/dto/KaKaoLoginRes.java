package com.loginApi.login.kakao.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class KaKaoLoginRes {

    //토큰 req
    private String token_type;
    private String access_token;
    private String id_token;        //req x
    private Integer expires_in;     //액세스 토큰 만료 시간
    private String refresh_token;    //사용자 리프레시 토큰 값
    private Integer refresh_token_expires_in; //리프레시 토큰 만료 시간(초)
    private String scope;           //인증된 사용자의 정보 조회 권한 범위


    private Long id;                //회원번호
//    private Integer expires_in;     //액세스 토큰 만료 시간
    private Integer app_id;         //토큰이 발급된 앱 id

}
