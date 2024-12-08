package com.onion.backend.controller;

import com.onion.backend.dto.ArticleCreateDto;
import com.onion.backend.dto.ArticleDto;
import com.onion.backend.entity.Article;
import com.onion.backend.entity.Board;
import com.onion.backend.service.ArticleService;
import com.onion.backend.service.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<Board>> fetchBoards(){
        return ResponseEntity.ok(boardService.fetchAllBoards());
    }

    @PostMapping("/{boardId}/articles")
    public ResponseEntity<Article> writeArticle(@PathVariable("boardId") Long boardId, @RequestBody ArticleCreateDto request){
        return ResponseEntity.ok(articleService.writeArticle(boardId, request));
    }

    @GetMapping("/{boardId}/articles")
    public ResponseEntity<List<ArticleDto>> getArticles(@PathVariable("boardId") Long boardId){
        return ResponseEntity.ok(articleService.firstGetArticle(boardId));
    }

}
