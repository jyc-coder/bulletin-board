package com.jycproject.bulletinboard.controller;

import com.jycproject.bulletinboard.dto.UserAccountDto;
import com.jycproject.bulletinboard.dto.request.ArticleCommentRequest;
import com.jycproject.bulletinboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
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
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest, Long articleId) {
            //TODO: 인증 정보를 넣어줘야한다
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of("jyc","pw","jyc@mail.com",null,null)));
        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId, Long articleId){
        //TODO: 인증 정보를 넣어줘야한다
        articleCommentService.deleteArticleComment(commentId);



        return "redirect:/articles/" + articleId ;
    }

}
