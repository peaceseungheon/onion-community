package com.onion.backend.service;

import com.onion.backend.entity.Article;
import com.onion.backend.entity.User;
import com.onion.backend.exception.ResourceNotFoundException;
import com.onion.backend.repository.UserRepository;
import com.onion.backend.repository.article.ArticleRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleValidateService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public boolean isCanWrite(long minute) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        Optional<User> opUser = userRepository.findByEmail(email);
        if (opUser.isEmpty()) {
            throw new ResourceNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Article article = articleRepository.getLatestCreateArticleByAuthor(opUser.get());

        if(article == null){
            return true;
        }

        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(article.getCreatedAt().plusMinutes(minute));
    }

    public boolean isCanUpdate(long minute) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        Optional<User> opUser = userRepository.findByEmail(email);
        if (opUser.isEmpty()) {
            throw new ResourceNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Article article = articleRepository.getLatestUpdateArticleByAuthor(opUser.get());
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(article.getUpdatedAt().plusMinutes(minute));
    }

}
