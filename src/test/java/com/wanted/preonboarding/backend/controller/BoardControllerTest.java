package com.wanted.preonboarding.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.backend.dto.BoardDto;
import com.wanted.preonboarding.backend.dto.SignupDto;
import com.wanted.preonboarding.backend.security.dto.properties.JWT;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BoardControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    private static String accessToken;
    private static final String SAVE_TITLE = "title";
    private static final String SAVE_DESCRIPTION = "description";
    private static final String WRITER_USERNAME = "@";

    private static final String ANOTHER_USERNAME = "@1";
    private static String anotherUserAccessToken;

    @Order(1)
    @Test
    @DisplayName("[회원가입 및 로그인]")
    void getAccessToken() throws Exception {
        String username = WRITER_USERNAME;
        String password = "testtest";

        String body = objectMapper.writeValueAsString(
                SignupDto.builder()
                        .password(password)
                        .username(username)
                        .build()
        );
        String anotherUserBody = objectMapper.writeValueAsString(
                SignupDto.builder()
                        .password(password)
                        .username(ANOTHER_USERNAME)
                        .build()
        );

        mvc.perform(MockMvcRequestBuilders.post("/signup").contentType(MediaType.APPLICATION_JSON).content(body))
                .andReturn();
        mvc.perform(MockMvcRequestBuilders.post("/signup").contentType(MediaType.APPLICATION_JSON).content(anotherUserBody))
                .andReturn();

        MvcResult loginResult = mvc.perform(MockMvcRequestBuilders.post("/login").content(body).contentType(MediaType.APPLICATION_JSON)).andReturn();

        MvcResult anotherUserLoginResult = mvc.perform(MockMvcRequestBuilders.post("/login").content(anotherUserBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        accessToken = loginResult.getResponse().getHeader(JWT.ACCESS_TOKEN_HEADER);
        anotherUserAccessToken = anotherUserLoginResult.getResponse().getHeader(JWT.ACCESS_TOKEN_HEADER);
    }

    @Test
    @DisplayName("[게시글][작성] 로그인 여부 - 로그인 X")
    @Order(2)
    void board_write_checking_login() throws Exception{
        //given
        String title = SAVE_TITLE;
        String description = SAVE_DESCRIPTION;

        String body = objectMapper.writeValueAsString(
                BoardDto.builder()
                        .title(title)
                        .description(description)
                        .build()
        );

        mvc.perform(MockMvcRequestBuilders
                        .post("/board")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("[게시글][작성] 로그인 여부 - 로그인 O")
    @Order(3)
    void board_write_checking_login_have_access_token() throws Exception{
        //given
        String title = SAVE_TITLE;
        String description = SAVE_DESCRIPTION;

        String body = objectMapper.writeValueAsString(
                BoardDto.builder()
                        .title(title)
                        .description(description)
                        .build()
        );


        mvc.perform(MockMvcRequestBuilders.post("/board")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JWT.ACCESS_TOKEN_HEADER, accessToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @DisplayName("[게시글][조회] 단일 조회")
    @Order(4)
    void board_find_detail() throws Exception{
        //given
        int id = 1;

        String expectBody = objectMapper.writeValueAsString(
                BoardDto.builder()
                        .title(SAVE_TITLE)
                        .description(SAVE_DESCRIPTION)
                        .username("@")
                        .build()
        );

        mvc.perform(MockMvcRequestBuilders.get("/board/"+id)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectBody));
    }


    void create_dummy_board() throws Exception{
        //given
        String title = SAVE_TITLE;
        String description = SAVE_DESCRIPTION;




        IntStream.rangeClosed(0, 40).forEach(num -> {
            try {
                String body = objectMapper.writeValueAsString(
                        BoardDto.builder()
                                .title(title + num)
                                .description(description)
                                .build()
                );
                mvc.perform(MockMvcRequestBuilders.post("/board")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JWT.ACCESS_TOKEN_HEADER, accessToken)
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @DisplayName("[게시글][조회] 페이징")
    @Order(5)
    void board_find_page() throws Exception{
        //given
        create_dummy_board();
        int page = 0;
        int size = 20;

        mvc.perform(MockMvcRequestBuilders.get("/board?page"+page+"&size="+size)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("[게시글][수정] 권한 체킹")
    @Order(6)
    void edit_board_authentication_check() throws Exception{
        //given
        String title = "UPDATE1";
        String description = SAVE_DESCRIPTION;

        String body = objectMapper.writeValueAsString(
                BoardDto.builder()
                        .title(title)
                        .description(description)
                        .build()
        );


        mvc.perform(MockMvcRequestBuilders.put("/board/1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JWT.ACCESS_TOKEN_HEADER, anotherUserAccessToken)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("[게시글][수정] 로그인 체킹")
    @Order(6)
    void edit_board_login_check() throws Exception{
        //given
        String title = "UPDATE1";
        String description = SAVE_DESCRIPTION;

        String body = objectMapper.writeValueAsString(
                BoardDto.builder()
                        .title(title)
                        .description(description)
                        .build()
        );


        mvc.perform(MockMvcRequestBuilders.put("/board/1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("[게시글][수정]")
    @Order(7)
    void edit_board() throws Exception{
        //given
        String title = "UPDATE1";
        String description = SAVE_DESCRIPTION;

        String body = objectMapper.writeValueAsString(
                BoardDto.builder()
                        .title(title)
                        .description(description)
                        .build()
        );


        mvc.perform(MockMvcRequestBuilders.put("/board/1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JWT.ACCESS_TOKEN_HEADER, accessToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        String expectBody = objectMapper.writeValueAsString(
                BoardDto.builder()
                        .title(title)
                        .description(description)
                        .username(WRITER_USERNAME)
                        .build()
        );

        mvc.perform(MockMvcRequestBuilders.get("/board/1")
                ).andExpect(MockMvcResultMatchers.content().string(expectBody));
    }

    @Test
    @DisplayName("[게시글][삭제] 권한 체킹")
    @Order(8)
    void remove_board_authentication_check() throws Exception{

        mvc.perform(MockMvcRequestBuilders.delete("/board/1")
                        .header(JWT.ACCESS_TOKEN_HEADER, anotherUserAccessToken)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @DisplayName("[게시글][삭제] 로그인 체킹")
    @Order(9)
    void remove_board_login_check() throws Exception{

        mvc.perform(MockMvcRequestBuilders.delete("/board/1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("[게시글][삭제]")
    @Order(10)
    void remove_board() throws Exception {

        mvc.perform(MockMvcRequestBuilders.delete("/board/1")
                        .header(JWT.ACCESS_TOKEN_HEADER, accessToken)
                )
                .andExpect(MockMvcResultMatchers.status().isAccepted());

        mvc.perform(MockMvcRequestBuilders.get("/board/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
