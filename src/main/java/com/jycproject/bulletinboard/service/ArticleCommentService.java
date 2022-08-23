package com.jycproject.bulletinboard.service;

import com.jycproject.bulletinboard.domain.ArticleComment;
import com.jycproject.bulletinboard.dto.ArticleCommentDto;
import com.jycproject.bulletinboard.repository.ArticleCommentRepository;
import com.jycproject.bulletinboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComment(long articleID) {
        return List.of();
    }

    public void saveArticleComment(ArticleCommentDto dto) {
    }
    public void updateArticleComment(ArticleCommentDto dto){

    }
    public void deleteArticleComment(Long articleCommentId){

    }
}

