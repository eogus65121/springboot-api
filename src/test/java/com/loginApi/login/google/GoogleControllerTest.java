package com.loginApi.login.google;


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
public class GoogleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("google init page")
    public void googInitTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/google/login"))
                .andDo(print())
                .andExpect(status().isSeeOther());
    }

}
