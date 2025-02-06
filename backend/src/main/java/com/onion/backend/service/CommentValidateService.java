package com.onion.backend.service;

import com.onion.backend.entity.Comment;
import com.onion.backend.entity.User;
import com.onion.backend.repository.UserRepository;
import com.onion.backend.repository.comment.CommentRepository;
import com.onion.backend.security.AuthenticationService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentValidateService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;

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
