package com.jycproject.bulletinboard.controller;

import com.jycproject.bulletinboard.config.SecurityConfig;
import com.jycproject.bulletinboard.config.TestSecurityConfig;
import com.jycproject.bulletinboard.domain.constant.FormStatus;
import com.jycproject.bulletinboard.domain.constant.SearchType;
import com.jycproject.bulletinboard.dto.ArticleDto;
import com.jycproject.bulletinboard.dto.ArticleWithCommentsDto;
import com.jycproject.bulletinboard.dto.UserAccountDto;
import com.jycproject.bulletinboard.dto.request.ArticleRequest;
import com.jycproject.bulletinboard.dto.response.ArticleResponse;
import com.jycproject.bulletinboard.dto.security.BoardPrincipal;
import com.jycproject.bulletinboard.service.ArticleService;
import com.jycproject.bulletinboard.service.PaginationService;
import com.jycproject.bulletinboard.util.FormDataEncoder;
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
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;
    private final FormDataEncoder formDataEncoder;

    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;

    public ArticleControllerTest(@Autowired MockMvc mvc,@Autowired FormDataEncoder formDataEncoder) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
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

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(articleService.searchArticles(eq(searchType), eq(searchValue),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4));
        // When & Then
        mvc.perform(get("/articles")
                        .queryParam("searchType",searchType.name())
                        .queryParam("searchValue",searchValue)
                )
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/index")) // 뷰의 존재여부 검사
                .andExpect(model().attributeExists("articles")) // 뷰에 모델 어트리뷰트로 넣어준 데이터존재 여부 검사
                .andExpect(model().attributeExists("searchTypes"));
        then(articleService).should().searchArticles(eq(searchType), eq(searchValue),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }


    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬기능")
    @Test
    public void givenPagingAndSortingParams_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
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

    @DisplayName("[view][GET] 게시글 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    public void givenNothing_whenRequestingArticlePage_thenRedirectsToLoginPage() throws Exception {
        // Given
        Long articleId = 1L;

        // When & Then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        then(articleService).shouldHaveNoInteractions();
        then(articleService).shouldHaveNoInteractions();
    }


    @WithMockUser
    @DisplayName("[view][GET] 게시글 페이지 - 정상 호출, 인증된 사용자")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        Long totalCount = 1L;
        given(articleService.getArticleWithComments(articleId)).willReturn(createArticleWithCommentsDto());
        given(articleService.getArticleCount()).willReturn(totalCount);
        // When & Then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/detail")) // 뷰의 존재여부 검사
                .andExpect(model().attributeExists("article")) // 뷰에 모델 어트리뷰트로 넣어준 데이터존재 여부 검사
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attribute("totalCount",totalCount)); // getArticleCount()호출 여부
        then(articleService).should().getArticleWithComments(articleId);
        then(articleService).should().getArticleCount();
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
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchHashtagView_thenReturnsArticleSearchHashtagView() throws Exception {
        // Given
        List<String> hashtags = List.of("#java","#spring","boot");
        given(articleService.searchArticlesViaHashtag(eq(null),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4,5));
        given(articleService.getHashtags()).willReturn(hashtags);
        // When & Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/search-hashtag")) // 뷰의 존재여부 검사
                .andExpect(model().attribute("articles",Page.empty()))
                .andExpect(model().attribute("hashtags",hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType",SearchType.HASHTAG));
        then(articleService).should().searchArticlesViaHashtag(eq(null),any(Pageable.class));
        then(articleService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }

    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출, 해시태그 입력")
    @Test
    public void givenHashtag_whenRequestingArticleSearchHashtagView_thenReturnsArticleSearchHashtagView() throws Exception {
        // Given
        String hashtag = "#java";
        List<String> hashtags = List.of("#java","#spring","boot");
        given(articleService.searchArticlesViaHashtag(eq(hashtag),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4,5));
        given(articleService.getHashtags()).willReturn(hashtags);
        // When & Then
        mvc.perform(get("/articles/search-hashtag")
                        .queryParam("searchValue",hashtag)
                )
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/search-hashtag")) // 뷰의 존재여부 검사
                .andExpect(model().attribute("articles",Page.empty()))
                .andExpect(model().attribute("hashtags",hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType",SearchType.HASHTAG));
        then(articleService).should().searchArticlesViaHashtag(eq(hashtag),any(Pageable.class));
        then(articleService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }
    @WithMockUser
    @DisplayName("[view][GET] 새 게시글 작성 페이지")
    @Test
    void givenNothing_whenRequesting_thenReturnsNewArticlePage() throws Exception {
        // Given
        String hashtag = "#java";

        // When & Then
        mvc.perform(get("/articles/form"))
                .andExpect(status().isOk()) // 정상 호출
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // 데이터 확인
                .andExpect(view().name("articles/form")) // 뷰의 존재여부 검사
                .andExpect(model().attribute("formStatus", FormStatus.CREATE));
    }
    @WithUserDetails(value="jycTest",userDetailsServiceBeanName = "userDetailsService",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 게시글 등록 - 정상 호출")
    @Test
    void givenNewArticleInfo_whenRequesting_thenSaveNewArticle() throws Exception {
        // Given
        ArticleRequest articleRequest = ArticleRequest.of("new title", "new content", "#new");
       willDoNothing().given(articleService).saveArticle(any(ArticleDto.class));

        // When & Then
        mvc.perform(
                post("/articles/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(articleRequest))
                        .with(csrf())

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"));
        then(articleService).should().saveArticle(any(ArticleDto.class));

    }

    @DisplayName("[view][GET] 게시글 수정 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    public void givenNothing_whenRequesting_thenRedirectsToLoginPage() throws Exception {
        // Given
        Long articleId = 1L;

        // When & Then
        mvc.perform(get("/articles/" + articleId + "/form"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        then(articleService).shouldHaveNoInteractions();
    }



    @WithMockUser
    @DisplayName("[view][GET] 게시글 수정 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void givenNothing_whenRequesting_thenReturnsUpdateArticlePage() throws Exception {
        // Given
       long articleId = 1L;
       ArticleDto dto = createArticleDto();
       given(articleService.getArticle(articleId)).willReturn(dto);

        // When & Then
        mvc.perform(get("/articles/" + articleId + "/form"))
                .andExpect(status().isOk())
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("article", ArticleResponse.from(dto)))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE));
        then(articleService).should().getArticle(articleId);

    }
    @WithUserDetails(value="jycTest",userDetailsServiceBeanName = "userDetailsService",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 수정 - 정상 호출")
    @Test
    void givenUpdatedArticleInfo_whenRequesting_thenUpdatesNewArticle() throws Exception {
        // Given
        long articleId = 1L;
        ArticleRequest articleRequest = ArticleRequest.of("new title", "new content", "#new");
        willDoNothing().given(articleService).updateArticle(eq(articleId),any(ArticleDto.class));

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId+ "/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(articleRequest))
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleService).should().updateArticle(eq(articleId),any(ArticleDto.class));

    }

    @WithUserDetails(value="jycTest",userDetailsServiceBeanName = "userDetailsService",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출")
    @Test
     void givenArticleIdToDelete_whenRequesting_thenDeletesArticle() throws Exception {
        // Given
        long articleId = 1L;
        String userId = "jycTest";
        willDoNothing().given(articleService).deleteArticle(articleId, userId);

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId+ "/delete")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"));
        then(articleService).should().deleteArticle(articleId,userId);

    }




    private ArticleWithCommentsDto createArticleWithCommentsDto() {
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

    private ArticleDto createArticleDto() {
        return ArticleDto.of(
                createUserAccountDto(),
                "title",
                "content",
                "#java"
        );
    }
}
