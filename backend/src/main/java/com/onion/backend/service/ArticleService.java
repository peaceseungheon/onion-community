package com.onion.backend.service;

import com.onion.backend.dto.ArticleCreateDto;
import com.onion.backend.dto.ArticleDto;
import com.onion.backend.dto.ArticleUpdateDto;
import com.onion.backend.entity.Article;
import com.onion.backend.entity.Board;
import com.onion.backend.entity.User;
import com.onion.backend.exception.NotAllowedException;
import com.onion.backend.exception.RateLimitException;
import com.onion.backend.exception.ResourceNotFoundException;
import com.onion.backend.repository.BoardRepository;
import com.onion.backend.repository.UserRepository;
import com.onion.backend.repository.article.ArticleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final BoardRepository boardRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public Article writeArticle(Long boardId, ArticleCreateDto request) {
        if(!isCanWrite()){
            throw new RateLimitException("글을 작성하려면 더 시간이 지나야 합니다.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        Optional<User> opUser = userRepository.findByEmail(email);
        if (opUser.isEmpty()) {
            throw new ResourceNotFoundException("로그인이 되어 있지 않습니다.");
        }
        User author = opUser.get();

        Optional<Board> opBoard = boardRepository.findById(boardId);
        if (opBoard.isEmpty()) {
            throw new ResourceNotFoundException("게시판이 존재하지 않습니다.");
        }

        Article article = Article.builder()
            .author(author)
            .title(request.getTitle())
            .content(request.getContent())
            .board(opBoard.get())
            .build();

        articleRepository.save(article);
        return article;
    }

    public List<ArticleDto> firstGetArticle(Long boardId) {
        return articleRepository.fetchOrderByCreateDateDesc(boardId)
            .stream()
            .map(ArticleDto::new)
            .collect(Collectors.toList());
    }

    public List<ArticleDto> getArticleBefore(Long boardId, Long lastId) {
        return articleRepository.fetchArticleBefore(boardId, lastId)
            .stream()
            .map(ArticleDto::new)
            .collect(Collectors.toList());
    }

    public List<ArticleDto> getArticleAfter(Long boardId, Long firstId) {
        return articleRepository.fetchArticleAfter(boardId, firstId)
            .stream()
            .map(ArticleDto::new)
            .collect(Collectors.toList());
    }

    public ArticleDto updateArticle(Long boardId, Long articleId, ArticleUpdateDto dto) {
        if(!isCanUpdate()){
            throw new RateLimitException("수정하려면 시간이 더 지나야 합니다.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        Article article = articleRepository.getByArticleId(articleId);
        boolean isAuthor = article.getAuthor().getEmail().equals(email);
        if (!isAuthor) {
            throw new NotAllowedException("수정은 작성자만 가능합니다.");
        }
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        return new ArticleDto(article);
    }

    public boolean isCanWrite() {
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
        return now.isAfter(article.getCreatedAt().plusMinutes(5));
    }

    public boolean isCanUpdate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        Optional<User> opUser = userRepository.findByEmail(email);
        if (opUser.isEmpty()) {
            throw new ResourceNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Article article = articleRepository.getLatestUpdateArticleByAuthor(opUser.get());
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(article.getCreatedAt().plusMinutes(5));
    }
}
