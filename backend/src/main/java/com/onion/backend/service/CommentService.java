package com.onion.backend.service;

import com.onion.backend.dto.CommentIn;
import com.onion.backend.dto.CommentOut;
import com.onion.backend.entity.Article;
import com.onion.backend.entity.Comment;
import com.onion.backend.entity.User;
import com.onion.backend.exception.ForbiddenException;
import com.onion.backend.exception.RateLimitException;
import com.onion.backend.repository.UserRepository;
import com.onion.backend.repository.article.ArticleRepository;
import com.onion.backend.repository.comment.CommentRepository;
import com.onion.backend.security.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentValidateService commentValidateService;
    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;

    @Transactional
    public CommentOut writeComment(Long articleId, CommentIn commentIn){
        Article article = articleRepository.getByArticleId(articleId);
        if(article.getIsDeleted()){
            throw new ForbiddenException("게시글이 삭제되었습니다.");
        }
        User user = userRepository.getByEmail(authenticationService.getAuthUserEmail());

        if(!commentValidateService.isCanWrite(1)){
            throw new RateLimitException("댓글 작성 후 1분 이후에 추가 댓글 작성이 가능합니다.");
        }
        Comment comment = Comment.builder()
            .article(article)
            .content(commentIn.getContent())
            .isDeleted(false)
            .user(user)
            .build();
        commentRepository.save(comment);
        return new CommentOut(comment);
    }

}
