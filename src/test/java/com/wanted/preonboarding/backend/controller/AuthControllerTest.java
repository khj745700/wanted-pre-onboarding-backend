package com.wanted.preonboarding.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.backend.dto.SignupDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    private static final String BASE_URL = "/signup";

    @Test
    @DisplayName("[회원가입] 이메일-패턴체크 (@ 미포함)")
    void signup_email_regex_not_exist() throws Exception {
        //given
        String username = "1";
        String password = "12345678";

        //when
        String body = objectMapper.writeValueAsString(
                SignupDto.builder()
                        .username(username)
                        .password(password)
                        .build()
        );

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("[회원가입] 이메일-패턴체크 (@ 포함)")
    void signup_email_regex_exist() throws Exception {
        //given
        String username = "@";
        String password = "12345678";

        //when
        String body = objectMapper.writeValueAsString(
                SignupDto.builder()
                        .username(username)
                        .password(password)
                        .build()
        );

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @DisplayName("[회원가입] 패스워드-패턴체크 (8자리 미만)")
    void signup_password_regex_under() throws Exception {
        //given
        String username = "@1";
        String password = "1234567";

        //when
        String body = objectMapper.writeValueAsString(
                SignupDto.builder()
                        .username(username)
                        .password(password)
                        .build()
        );

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("[회원가입] 패스워드-패턴체크 (8자리 이상)")
    void signup_password_regex_up() throws Exception {
        //given
        String username = "@1";
        String password = "12345678";

        //when
        String body = objectMapper.writeValueAsString(
                SignupDto.builder()
                        .username(username)
                        .password(password)
                        .build()
        );

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @DisplayName("[회원가입]중복회원 회원가입 거절")
    void signup_duplicate_user_denied() throws Exception{
        //given
        String username = "@2";
        String password = "12345678";

        //when
        String body = objectMapper.writeValueAsString(
                SignupDto.builder()
                        .username(username)
                        .password(password)
                        .build()
        );

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted());

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
