package com.loginApi.login.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.loginApi.login.kakao.constant.KaKaoConfigUtils;
import com.loginApi.login.kakao.dto.KaKaoLoginDto;
import com.loginApi.login.kakao.dto.KaKaoLoginReq;
import com.loginApi.login.kakao.dto.KaKaoLoginRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequiredArgsConstructor
@RequestMapping(value="/kakao")
@Slf4j
public class KakaoController {

    private final KaKaoConfigUtils configUtils;

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ResponseEntity<Object> moveKakaoInitUrl(){
        String authUrl = configUtils.kakaoInitUrl();
        URI redirectUri = null;

        try{
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(value="/login/redirect", method=RequestMethod.GET)
    public ResponseEntity<KaKaoLoginDto> redirectKakaoLogin(@RequestParam(value = "code") String authCode){
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> reqParams = new LinkedMultiValueMap<>();
        reqParams.add("grant_type", "authorization_code");
        reqParams.add("client_id", configUtils.getKakaoClientId());
        reqParams.add("redirect_uri", configUtils.getKakaoRedirectUrl());
        reqParams.add("code", authCode);
        reqParams.add("client_secret", configUtils.getKakaoSecret());

//        KaKaoLoginReq requestParam = KaKaoLoginReq.builder()
//                .grant_type("authorization_code")
//                .client_id(configUtils.getKakaoClientId())
//                .redirect_uri(configUtils.getKakaoRedirectUrl())
//                .code(authCode)
////                .client_secret(configUtils.getKakaoSecret())
//                .build();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity httpEntity = new HttpEntity<>(reqParams, headers);

            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(configUtils.getKakaoAuthUrl(), httpEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            KaKaoLoginRes kakaoLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<KaKaoLoginRes>() {
            });

//            String jwtToken = kakaoLoginResponse.getId_token();
            String accessToken = kakaoLoginResponse.getAccess_token();
            String requestUrl = UriComponentsBuilder.fromHttpUrl(configUtils.getKakaoLoginCheckUrl()).toUriString();
            String reqToken = "Bearer " + accessToken;
//            String requestUrl = configUtils.getKakaoLoginCheckUrl();
            HttpHeaders getHeaders = new HttpHeaders();
            getHeaders.set("Authorization", reqToken);
            HttpEntity reqEntity = new HttpEntity<>(headers);

            /** jwtToken, accessToken 발급까지 성공, 이후 GET으로 데이터를 가져오는 과정에서 에러
             * error message : java.lang.IllegalArgumentException: invalid start or end
             */

            ResponseEntity<String> respEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, reqEntity, String.class);
            String resultJson = respEntity.getBody();
            log.info(resultJson);

            if (resultJson != null) {
                KaKaoLoginDto kakaoInfoDto = objectMapper.readValue(resultJson, new TypeReference<KaKaoLoginDto>() {
                });
                return ResponseEntity.ok().body(kakaoInfoDto);
            } else {
                throw new Exception("kakao oauth fail");
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body(null);
    }

}
