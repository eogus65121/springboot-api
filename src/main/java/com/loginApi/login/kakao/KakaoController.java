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
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequiredArgsConstructor
@RequestMapping(value="/kakao")
public class KakaoController {

    private final KaKaoConfigUtils configUtils;

    private final WebClient webClient;

    @RequestMapping(value="/oauth/authorize", method = RequestMethod.GET)
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

        KaKaoLoginReq requestParam = KaKaoLoginReq.builder()
                .grant_type("authorization_code")
                .client_id(configUtils.getKakaoClientId())
                .redirect_uri(configUtils.getKakaoRedirectUrl())
                .code(authCode)
                .build();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<KaKaoLoginReq> httpEntity = new HttpEntity<>(requestParam);
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(configUtils.getKakaoAuthUrl() + "/token", httpEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            KaKaoLoginRes kakaoLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<KaKaoLoginRes>() {
            });

            String jwtToken = kakaoLoginResponse.getId_token();
            String requestUrl = UriComponentsBuilder.fromHttpUrl(configUtils.getKakaoAuthUrl() + "/access_token_info").queryParam("access_token", jwtToken).toUriString();

            String resultJson = restTemplate.getForObject(requestUrl, String.class);

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
