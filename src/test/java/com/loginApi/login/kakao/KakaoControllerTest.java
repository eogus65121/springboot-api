package com.loginApi.login.kakao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class KakaoControllerTest {

    @Autowired(required = false)
    private MockMvc mockMvc;

    @Test
    @DisplayName("kakao login init url request")
    public void kakaoInitTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/kakao/login"))
                .andDo(print())
                .andExpect(status().isSeeOther());
    }

}
