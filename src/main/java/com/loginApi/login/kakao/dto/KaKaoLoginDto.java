package com.loginApi.login.kakao.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class KaKaoLoginDto {

    //ID 토큰 페이로드
    private String iss;         //ID 토큰을 발급한 인증 기관 정보
    private String aud;         //ID 토큰이 발급된 앱의 앱키
    private String sub;         //ID 토큰에 해당하는 사용자의 회원 번호
    private Integer iat;        //ID토큰 발급 또는 갱신 시각
    private Integer exp;        //ID토큰 만료 시간
    private Integer auth_time;  //로그인을 통해 인증을 완료한 시각
    private String nonce;       //인가 코드 받기 요청 시 전달한 데이터 > ID 토큰 유효성 검사에 사용
}
