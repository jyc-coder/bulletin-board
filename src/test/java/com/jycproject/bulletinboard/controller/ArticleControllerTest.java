package com.jycproject.bulletinboard.controller;

import com.jycproject.bulletinboard.config.SecurityConfig;
import com.jycproject.bulletinboard.dto.ArticleWithCommentsDto;
import com.jycproject.bulletinboard.dto.UserAccountDto;
import com.jycproject.bulletinboard.service.ArticleService;
import com.jycproject.bulletinboard.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
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
    @MockBean private PaginationService paginationService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        given(articleService.searchArticles(eq(null),eq(null),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4));
        // When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/index")) // 뷰의 존재여부 검사
                .andExpect(model().attributeExists("articles")) // 뷰에 모델 어트리뷰트로 넣어준 데이터존재 여부 검사
                .andExpect(model().attributeExists("paginationBarNumbers")); // 뷰에 모델 어트리뷰트로 넣어준 데이터존재 여부 검사
        then(articleService).should().searchArticles(eq(null),eq(null),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬기능")
    @Test
    public void givenPagingAndSortingParams_whenSearchingArticlesPage_thenReturnsArticlesPage() throws Exception {
        // Given
        String sortName ="title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1,2,3,4,5);
        given(articleService.searchArticles(null,null,pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(),Page.empty().getTotalPages())).willReturn(barNumbers);
        // When & Then
        mvc.perform(
                get("/articles")
                        .queryParam("page",String.valueOf(pageNumber))
                        .queryParam("size",String.valueOf(pageSize))
                        .queryParam("sort",sortName +"," + direction)
                )
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/index")) // 뷰의 존재여부 검사
                .andExpect(model().attributeExists("articles")) // 뷰에 모델 어트리뷰트로 넣어준 데이터존재 여부 검사
                .andExpect(model().attribute("paginationBarNumbers",barNumbers)); // paginationBarNumbers의 값이 barNumbers와 일치하는가?
        then(articleService).should().searchArticles(eq(null),eq(null),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(),Page.empty().getTotalPages()); // getPaginationBarNumbers 호출 확인
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
