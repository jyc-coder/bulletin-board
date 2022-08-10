package com.jycproject.bulletinboard.repository;

import com.jycproject.bulletinboard.config.JpaConfig;
import com.jycproject.bulletinboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@WebAppConfiguration
@Import(JpaConfig.class)
@DataJpaTest
@DisplayName("JPA 연결 테스트")
class JpaRepositoryTest {


    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
           @Autowired ArticleRepository articleRepository,
           @Autowired ArticleCommentRepository articleCommentRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }


    @Test
    @DisplayName("select 테스트")
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Article> articles = articleRepository.findAll();

        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }


    @Test
    @DisplayName("insert 테스트")
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count();


        // When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));
        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);

    }

    @Test
    @DisplayName("update 테스트")
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        // When
        Article savedArticle = articleRepository.save(article);
        articleRepository.flush();
        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);

    }

    @Test
    @DisplayName("delete 테스트")
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size();


        // When
        articleRepository.delete(article);
        // Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount -1);
        assertThat(articleCommentRepository.count()).isEqualTo((previousArticleCommentCount - deletedCommentSize));

    }

}
