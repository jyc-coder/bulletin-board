package com.jycproject.bulletinboard.service;

import com.jycproject.bulletinboard.domain.Article;
import com.jycproject.bulletinboard.domain.ArticleComment;
import com.jycproject.bulletinboard.domain.UserAccount;
import com.jycproject.bulletinboard.dto.ArticleCommentDto;
import com.jycproject.bulletinboard.dto.ArticleUpdateDto;
import com.jycproject.bulletinboard.dto.UserAccountDto;
import com.jycproject.bulletinboard.repository.ArticleCommentRepository;
import com.jycproject.bulletinboard.repository.ArticleRepository;
import com.jycproject.bulletinboard.repository.UserAccountRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks
    private ArticleCommentService sut;

    @Mock
    private ArticleCommentRepository articleCommentRepository;
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserAccountRepository userAccountRepository;


    @DisplayName("게시글 ID를 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsComments(){
        // Given
        Long articleId = 1L;
        ArticleComment expected = createArticleComment("content");
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));
        // When
        List<ArticleCommentDto> actual = sut.searchArticleComment(articleId);
        // Then
        assertThat(actual).hasSize(1)
                .first().hasFieldOrPropertyWithValue("content",expected.getContent());
        then(articleCommentRepository).should().findByArticle_Id(articleId);

    }



    @DisplayName("댓글 정보를 입력하면, 해당하는 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment(){
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
        // When
        sut.saveArticleComment(dto);
        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }
    @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안한다.")
    @Test
    void givenNonexistentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing(){
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);
        // When
        sut.saveArticleComment(dto);
        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(userAccountRepository).shouldHaveNoInteractions();
        then(articleCommentRepository).shouldHaveNoInteractions();
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 수정한다.")
    @Test
    void givenArticleCommentInfo_whenUpdatingArticleComment_thenUpdatesArticleComment(){
        // Given
        String oldContent = "content";
        String updatedContent = "댓글";
        ArticleComment articleComment = createArticleComment(oldContent);
        ArticleCommentDto dto = createArticleCommentDto(updatedContent);
        given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);
        // When
        sut.updateArticleComment(dto);
        // Then
        assertThat(articleComment.getContent())
                .isNotEqualTo(oldContent)
                .isEqualTo(updatedContent);
        then(articleCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무것도 안한다.")
    @Test
    void giveNonexistentArticleComment_whenUpdatingArticleComment_thenLogsWarningAndDoesNothing(){
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);
        // When
        sut.updateArticleComment(dto);
        // Then
        then(articleCommentRepository).should().getReferenceById(dto.id());

    }

    @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
    @Test
    void giveArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment(){
        // Given
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentRepository).deleteById(articleCommentId);
        // When
        sut.deleteArticleComment(articleCommentId);
        // Then
        then(articleCommentRepository).should().deleteById(articleCommentId);

    }



    private ArticleComment createArticleComment(String content) {
       return ArticleComment.of(
               Article.of(createUserAccount(),"title","content","hashtag"),
               createUserAccount(),
               content
       );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "jyc",
                "password",
                "jyc@email.com",
                "Jyc",
                null
        );
    }

    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                content,
                LocalDateTime.now(),
                "jyc",
                LocalDateTime.now(),
                "jyc"
        );

    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "jyc",
                "password",
                "jyc@mail.com",
                "Jyc",
                "This is memo",
                LocalDateTime.now(),
                "jyc",
                LocalDateTime.now(),
                "jyc"
        );
    }

    private Article createArticle() {
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }

}