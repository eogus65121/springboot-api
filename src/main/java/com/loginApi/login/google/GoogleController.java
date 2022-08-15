package com.loginApi.login.google;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.loginApi.login.google.constant.GoogleConfigUtils;
import com.loginApi.login.google.dto.GoogleLoginDto;
import com.loginApi.login.google.dto.GoogleLoginReq;
import com.loginApi.login.google.dto.GoogleLoginRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequiredArgsConstructor
@RequestMapping(value="/google")
public class GoogleController {

    private final GoogleConfigUtils configUtils;

    @GetMapping(value = "/login")
    public ResponseEntity<Object> moveGoogleInitUrl(){
        String authUrl = configUtils.googleInitUrl();
        URI redirectUri = null;

        try{
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            /**
             HttpStatus.SEE_OTHER == code 303
             code 303 == 요청자가 다른 위치에 별도의 GET 요청을 하여 응답을 검색할 경우 서버는 이 코드를 표시한다. HEAD 요청 이외의 모든 요청을 다른 위치로 자동으로 전달한다
             */
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/login/redirect")
    public ResponseEntity<GoogleLoginDto> redirectGoogleLogin(@RequestParam(value = "code") String authCode){

        RestTemplate restTemplate = new RestTemplate();

        //요청 param 생성
        GoogleLoginReq requestParam = GoogleLoginReq.builder()
                .clientId(configUtils.getGoogleClientId())
                .clientSecret(configUtils.getGoogleSecret())
                .code(authCode)
                .redirectUri(configUtils.getGoogleRedirectUrl())
                .grantType("authorization_code")
                .build();

        try {
            // header 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GoogleLoginReq> httpReqEntity = new HttpEntity<>(requestParam, headers);
            // post 쏘기
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(configUtils.getGoogleAuthUrl() + "/token", httpReqEntity, String.class);

            // String to Object 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // null이 아닌 경우에만
            GoogleLoginRes googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleLoginRes>() {
            });

            // 사용자의 정보는 jwt token으로 저장, id_token에 값을 저장
            String jwtToken = googleLoginResponse.getIdToken();

            // JWT token을 전달해 jwt 저장된 사용자 정보 확인
            String requestUrl = UriComponentsBuilder.fromHttpUrl(configUtils.getGoogleAuthUrl() + "/tokeninfo").queryParam("id_token", jwtToken).toUriString();

            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            if (resultJson != null) {
                GoogleLoginDto userInfoDto = objectMapper.readValue(resultJson, new TypeReference<GoogleLoginDto>() {
                });
                return ResponseEntity.ok().body(userInfoDto);
            } else {
                throw new Exception("google oauth fail");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body(null);
    }

}
