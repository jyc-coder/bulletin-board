package com.jycproject.bulletinboard.controller;

import com.jycproject.bulletinboard.config.SecurityConfig;
import com.jycproject.bulletinboard.dto.ArticleWithCommentsDto;
import com.jycproject.bulletinboard.dto.UserAccountDto;
import com.jycproject.bulletinboard.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        given(articleService.searchArticles(eq(null),eq(null),any(Pageable.class))).willReturn(Page.empty());
        // When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/index")) // 뷰의 존재여부 검사
                .andExpect(model().attributeExists("articles")); // 뷰에 모델 어트리뷰트로 넣어준 데이터존재 여부 검사
        then(articleService).should().searchArticles(eq(null),eq(null),any(Pageable.class));
    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleCommentsDto());
        // When & Then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/detail")) // 뷰의 존재여부 검사
                .andExpect(model().attributeExists("article")) // 뷰에 모델 어트리뷰트로 넣어준 데이터존재 여부 검사
                .andExpect(model().attributeExists("articleComments"));
        then(articleService).should().getArticle(articleId);
    }



    @Disabled("구현 중")@DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/search")); // 뷰의 존재여부 검사
    }
    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/search-hashtag")); // 뷰의 존재여부 검사
    }

    private ArticleWithCommentsDto createArticleCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "jyc",
                LocalDateTime.now(),
                "jyc"

        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "jyc",
                "pw",
                "jyc@mail.com",
                "Jyc",
                "memo",
                LocalDateTime.now(),
                "jyc",
                LocalDateTime.now(),
                "jyc"
        );
    }
}
