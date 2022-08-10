package com.jycproject.bulletinboard.repository;

import com.jycproject.bulletinboard.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
