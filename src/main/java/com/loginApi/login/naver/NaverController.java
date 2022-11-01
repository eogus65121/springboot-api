package com.loginApi.login.naver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loginApi.login.naver.constant.NaverConfigUtils;
import com.loginApi.login.naver.dto.NaverTokenRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/naver")
public class NaverController {

    private final NaverConfigUtils naverConfigUtils;

    @GetMapping(value="/login")
    public ResponseEntity<Object> moveNaverInitUrl(){
        String authUrl = naverConfigUtils.getNaverAuthUrl();
        authUrl += "&client_id=" + naverConfigUtils.getClientId();
        authUrl += "&state=" + naverConfigUtils.getStatInfo();
        authUrl += "&redirect_uri=" + naverConfigUtils.getCallbackUrl();
        HttpHeaders headers = new HttpHeaders();
        URI uri = null;
        try{
            uri = new URI(authUrl);
            headers.setLocation(uri);
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/callback")
    public String callbackNaverAuthReq(@RequestParam(value = "code") String code, @RequestParam(value = "state") String state){
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("grant_type", "authorization_code");
        paramMap.add("client_id", naverConfigUtils.getClientId());
        paramMap.add("client_secret", naverConfigUtils.getSecret());
        paramMap.add("code", code);
        paramMap.add("state", state);

        try{
            ResponseEntity<String> tokenResponse = restTemplate.postForEntity(naverConfigUtils.getNaverTokenUrl(),paramMap, String.class);
            ObjectMapper mapper = new ObjectMapper();

            NaverTokenRespDto naverTokenRespDto = mapper.readValue(tokenResponse.getBody(), new TypeReference<NaverTokenRespDto>() {
            });

            System.out.println(naverTokenRespDto.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }
}
