package com.jycproject.bulletinboard.repository.querydsl;

import com.jycproject.bulletinboard.domain.Article;
import com.jycproject.bulletinboard.domain.QArticle;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;
        JPQLQuery<String> query = from(article)
                .distinct()
                .select(article.hashtag)
                .where(article.hashtag.isNotNull());

        return query.fetch();

    }
}
