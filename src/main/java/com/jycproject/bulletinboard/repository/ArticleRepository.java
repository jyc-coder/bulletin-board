package com.jycproject.bulletinboard.repository;

import com.jycproject.bulletinboard.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}