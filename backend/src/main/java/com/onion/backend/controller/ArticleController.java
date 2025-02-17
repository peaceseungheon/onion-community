package com.onion.backend.controller;

import com.onion.backend.dto.ArticleCreateDto;
import com.onion.backend.dto.ArticleDto;
import com.onion.backend.dto.ArticleUpdateDto;
import com.onion.backend.entity.Board;
import com.onion.backend.service.ArticleService;
import com.onion.backend.service.BoardService;
import com.onion.backend.service.CommentService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Board>> fetchBoards() {
        return ResponseEntity.ok(boardService.fetchAllBoards());
    }

    @PostMapping("/{boardId}/articles")
    public ResponseEntity<ArticleDto> writeArticle(@PathVariable("boardId") Long boardId,
        @RequestBody ArticleCreateDto request) {
        return ResponseEntity.ok(articleService.writeArticle(boardId, request));
    }

    @GetMapping("/{boardId}/articles")
    public ResponseEntity<List<ArticleDto>> getArticles(@PathVariable("boardId") Long boardId,
        @RequestParam(value = "lastId", required = false) Long lastId,
        @RequestParam(value = "firstId", required = false) Long firstId) {

        if (lastId != null) {
            return ResponseEntity.ok(articleService.getArticleBefore(boardId, lastId));
        }

        if (firstId != null) {
            return ResponseEntity.ok(articleService.getArticleAfter(boardId, firstId));
        }

        return ResponseEntity.ok(articleService.firstGetArticle(boardId));
    }

    @PutMapping("/{boardId}/articles/{articleId}")
    public void updateArticle(@PathVariable("boardId") Long boardId,
        @PathVariable("articleId") Long articleId, @RequestBody
        ArticleUpdateDto dto) {
        ResponseEntity.ok(articleService.updateArticle(boardId, articleId, dto));
    }

    @DeleteMapping("/{boardId}/articles/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("boardId") Long boardId,
        @PathVariable("articleId") Long articleId) {
        articleService.deleteArticle(boardId, articleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId}/articles/{articleId}")
    public ResponseEntity<ArticleDto> getArticle(@PathVariable("boardId") Long boardId,
        @PathVariable("articleId") Long articleId){
       /* CompletableFuture<ArticleDto> articleWithComments = commentService
            .getArticleWithComments(boardId, articleId);
        return ResponseEntity.ok(articleWithComments.join());*/
        return ResponseEntity.ok(commentService.getArticle(boardId, articleId));
    }

}
