package com.loginApi.login.naver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@Builder
public class NaverAuthRespDto {
    private String code;
    private String state;
    private String error;
    private String error_description;
}
