package com.onion.backend.controller;

import com.onion.backend.dto.CommentIn;
import com.onion.backend.dto.CommentOut;
import com.onion.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/boards/{boardId}/articles/{articleId}/comment")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentOut> writeComment(@PathVariable("boardId") Long boardId,
        @PathVariable("articleId") Long articleId, @RequestBody CommentIn commentIn) {
        return ResponseEntity.ok(commentService.writeComment(articleId, commentIn));
    }

    @PutMapping
    public ResponseEntity<CommentOut> editComment(
        @PathVariable("boardId") Long boardId,
        @PathVariable("articleId") Long articleId,
        @RequestBody CommentIn commentIn) {
        return ResponseEntity.ok(commentService.editComment(articleId, commentIn));
    }

}
