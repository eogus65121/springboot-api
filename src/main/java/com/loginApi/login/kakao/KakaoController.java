package com.loginApi.login.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.loginApi.login.kakao.constant.KaKaoConfigUtils;
import com.loginApi.login.kakao.dto.KaKaoLoginRes;
import com.loginApi.login.kakao.dto.KaKaoLoginStatRes;
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
    public ResponseEntity<KaKaoLoginStatRes> redirectKakaoLogin(@RequestParam(value = "code") String authCode){
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> reqParams = new LinkedMultiValueMap<>();
        reqParams.add("grant_type", "authorization_code");
        reqParams.add("client_id", configUtils.getKakaoClientId());
        reqParams.add("redirect_uri", configUtils.getKakaoRedirectUrl());
        reqParams.add("code", authCode);
        reqParams.add("client_secret", configUtils.getKakaoSecret());


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

            String accessToken = kakaoLoginResponse.getAccess_token();
            String requestUrl = configUtils.getKakaoLoginCheckUrl();

            HttpHeaders getHeader = new HttpHeaders();
            getHeader.set("Authorization", "Bearer " + accessToken);
            HttpEntity entity = new HttpEntity<>(getHeader);

            ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);
            String resultJson = responseEntity.getBody();

            if (resultJson != null) {
                KaKaoLoginStatRes kakaoLoginInfo = objectMapper.readValue(resultJson, new TypeReference<KaKaoLoginStatRes>() {
                });

                return ResponseEntity.ok().body(kakaoLoginInfo);
            } else {
                throw new Exception("kakao oauth fail");
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body(null);
    }

}
