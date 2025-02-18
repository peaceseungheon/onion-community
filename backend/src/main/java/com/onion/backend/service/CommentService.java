package com.onion.backend.service;

import com.onion.backend.dto.ArticleDto;
import com.onion.backend.dto.CommentIn;
import com.onion.backend.dto.CommentOut;
import com.onion.backend.entity.Article;
import com.onion.backend.entity.Comment;
import com.onion.backend.entity.User;
import com.onion.backend.exception.ForbiddenException;
import com.onion.backend.exception.RateLimitException;
import com.onion.backend.exception.ResourceNotFoundException;
import com.onion.backend.repository.UserRepository;
import com.onion.backend.repository.article.ArticleRepository;
import com.onion.backend.repository.comment.CommentRepository;
import com.onion.backend.security.AuthenticationService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentValidateService commentValidateService;
    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;
    private final ArticleService articleService;

    @Transactional
    public CommentOut writeComment(Long articleId, CommentIn commentIn) {
        Article article = articleRepository.getByArticleId(articleId);
        if (article.getIsDeleted()) {
            throw new ForbiddenException("게시글이 삭제되었습니다.");
        }
        User user = userRepository.getByEmail(authenticationService.getAuthUserEmail());

        if (!commentValidateService.isCanWrite(1)) {
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

    @Async
    public CompletableFuture<List<Comment>> getComments(Long articleId) {
        List<Comment> comments = commentRepository.getComments(articleId);
        return CompletableFuture.completedFuture(comments);
    }

    public CompletableFuture<ArticleDto> getArticleWithComments(Long boardId, Long articleId) {
        CompletableFuture<Article> articleResult = articleService.getArticle(boardId, articleId);
        CompletableFuture<List<Comment>> commentsResult = getComments(articleId);
        CompletableFuture<Void> combinedFuture = CompletableFuture
            .allOf(articleResult, commentsResult);
        return combinedFuture.thenApply(voidRes -> {
            Article article = articleResult.join();
            List<Comment> comments = commentsResult.join();
            return new ArticleDto(article, comments);
        });
    }

    public ArticleDto getArticle(Long boardId, Long articleId){
        Article article = articleRepository.getByArticleId(articleId);
        List<Comment> comments = commentRepository.getComments(article.getId());
        return new ArticleDto(article, comments);
    }

    @Transactional
    public CommentOut editComment(Long articleId, CommentIn commentIn) {
        Comment comment = commentValidateService.validation(articleId, commentIn.getCommentId());
        comment.edit(commentIn.getContent());
        return new CommentOut(comment);
    }

    @Transactional
    public void deleteComment(Long articleId, Long commentId) {
        Comment comment = commentValidateService.validation(articleId, commentId);
        comment.delete();
    }
}
