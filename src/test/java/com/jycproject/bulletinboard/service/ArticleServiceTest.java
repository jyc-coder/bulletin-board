package com.jycproject.bulletinboard.service;

import com.jycproject.bulletinboard.domain.Article;
import com.jycproject.bulletinboard.domain.type.SearchType;
import com.jycproject.bulletinboard.dto.ArticleDto;
import com.jycproject.bulletinboard.dto.ArticleUpdateDto;
import com.jycproject.bulletinboard.repository.ArticleRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import javax.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;

    @Mock
    private ArticleRepository articleRepository;




//    정렬 기능
    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticles() {
        // Given

        // When
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword"); // 제목, 본문, ID, 닉네임, 해시태그
        // Then
        assertThat(articles).isNotNull();

    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        // Given

        // When
        ArticleDto articles = sut.searchArticle(1L); // 제목, 본문, ID, 닉네임, 해시태그
        // Then
        assertThat(articles).isNotNull();

    }

    @DisplayName("게시글 정보를 입력하면 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle(){
        // Given
        given(articleRepository.save(any(Article.class))).willReturn(null);

        // When
        sut.saveArticle(ArticleDto.of(LocalDateTime.now(), "Jyc", "title", "content","#java"));
        // Then

        then(articleRepository).should().save(any(Article.class)); // save 메소드가 호출되었는지 여부를 확인
    }

    @DisplayName("게시글 ID와 수정정보를 입력하면 게시글을 수정한다.")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle(){
        // Given
        given(articleRepository.save(any(Article.class))).willReturn(null);

        // When
        sut.updateArticle(1L, ArticleUpdateDto.of("title", "content","#java"));
        // Then

        then(articleRepository).should().save(any(Article.class)); // save 메소드가 호출되었는지 여부를 확인
    }

    @DisplayName("게시글 ID를 입력하면 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle(){
        // Given
        willDoNothing().given(articleRepository).delete(any(Article.class));

        // When
        sut.deleteArticle(1L);
        // Then

        then(articleRepository).should().delete(any(Article.class)); // delete 메소드가 호출되었는지 여부를 확인
    }






}