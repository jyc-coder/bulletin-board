package com.jycproject.bulletinboard.dto.request;

import com.jycproject.bulletinboard.dto.ArticleCommentDto;
import com.jycproject.bulletinboard.dto.UserAccountDto;

public record ArticleCommentRequest(Long articleId, String content) {

    public static ArticleCommentRequest of (Long articleId, String content) {
       return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto){
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                content
        );
    }
}
