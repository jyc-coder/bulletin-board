package com.jycproject.bulletinboard.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Data REST - API 테스트")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class DataRestTest {
    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void GivenNothing_whenRequestingArticles_thenReturnsArticlesJson() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(result -> content().contentType(MediaType.valueOf("application/hal+json")));

    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void GivenNothing_whenRequestingArticle_thenReturnsArticleJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(result -> content().contentType(MediaType.valueOf("application/hal+json")));

    }

    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    void GivenNothing_whenRequestingArticleCommentsFromArticle_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(status().isOk())
                .andExpect(result -> content().contentType(MediaType.valueOf("application/hal+json")));

    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void GivenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments"))
                .andExpect(status().isOk())
                .andExpect(result -> content().contentType(MediaType.valueOf("application/hal+json")));

    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void GivenNothing_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk())
                .andExpect(result -> content().contentType(MediaType.valueOf("application/hal+json")));

    }
}
