package com.jycproject.bulletinboard.repository;

import com.jycproject.bulletinboard.domain.ArticleComment;
import com.jycproject.bulletinboard.domain.QArticle;
import com.jycproject.bulletinboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root){
        bindings.excludeUnlistedProperties(true); // 리스팅을 하지 않은 프로퍼티를 검색에서 제외한다.
        bindings.including(root.content, root.createdAt, root.createdBy); // 엔티티에 있는 필드중, 해당 항목들도 검색조건으로 검색할수 있게 해준다.
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // like'%${v}%'
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    };
}
