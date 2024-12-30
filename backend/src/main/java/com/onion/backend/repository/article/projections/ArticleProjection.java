package com.onion.backend.repository.article.projections;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class ArticleProjection {

    private Long boardId;
    private Long articleId;
    private String title;
    private String content;
    private String authorName;
    private Long authorNo;
    private String createDate;

    public ArticleProjection(Long boardId, Long articleId, String title, String content,
        String authorName, Long authorNo, LocalDateTime createDate) {
        this.boardId = boardId;
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.authorNo = authorNo;
        this.createDate = createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
