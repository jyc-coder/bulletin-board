package com.jycproject.bulletinboard.controller;

import com.jycproject.bulletinboard.dto.UserAccountDto;
import com.jycproject.bulletinboard.dto.request.ArticleCommentRequest;
import com.jycproject.bulletinboard.dto.security.BoardPrincipal;
import com.jycproject.bulletinboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {
    private final ArticleCommentService articleCommentService;


    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest,
                                        Long articleId,
                                        @AuthenticationPrincipal BoardPrincipal boardPrincipal
                                        ) {
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(boardPrincipal.toDto()));
        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId,
                                       Long articleId,
                                       @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ){
        articleCommentService.deleteArticleComment(commentId, boardPrincipal.username());


        return "redirect:/articles/" + articleId ;
    }

}
