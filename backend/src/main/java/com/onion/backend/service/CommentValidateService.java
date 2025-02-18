package com.onion.backend.service;

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
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentValidateService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;
    private final ArticleRepository articleRepository;

    public Comment validation(Long articleId, Long commentId){
        Article article = articleRepository.getByArticleId(articleId);
        if (article.getIsDeleted()) {
            throw new ForbiddenException("게시글이 삭제되었습니다.");
        }
        User user = userRepository.getByEmail(authenticationService.getAuthUserEmail());
        if(!this.isCanUpdate(1)){
            throw new RateLimitException("1분 후에 댓글 수정이 가능합니다.");
        }

        Optional<Comment> opComment = commentRepository.findById(commentId);
        if(opComment.isEmpty()){
            throw new ResourceNotFoundException("댓글 정보가 존재하지 않습니다.");
        }
        Comment comment = opComment.get();
        if(comment.getIsDeleted()){
            throw new ForbiddenException("삭제된 댓글입니다.");
        }
        if(!comment.getUser().getId().equals(user.getId())){
            throw new ForbiddenException("댓글 작성자만 수정 가능합니다.");
        }
        return comment;
    }

    public boolean isCanWrite(long minute) {
        String email = authenticationService.getAuthUserEmail();

        User user = userRepository.getByEmail(email);
        Comment comment = commentRepository
            .getLatestCreateCommentByAuthor(user);

        if(comment == null){
            return true;
        }
        return LocalDateTime.now().isAfter(comment.getCreatedAt().plusMinutes(minute));
    }

    public boolean isCanUpdate(long minute) {
        String email = authenticationService.getAuthUserEmail();

        User user = userRepository.getByEmail(email);
        Comment comment = commentRepository
            .getLatestCreateCommentByAuthor(user);

        if(comment == null){
            return true;
        }
        return LocalDateTime.now().isAfter(comment.getUpdatedAt().plusMinutes(minute));
    }

}
