package com.onion.backend.dto;


import com.onion.backend.repository.article.projections.ArticleProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {

    private Long boardId;
    private Long articleId;
    private String title;
    private String content;
    private String authorName;
    private String createDate;

    public ArticleDto(ArticleProjection articleProjection){
        this.boardId = articleProjection.getBoardId();
        this.articleId = articleProjection.getArticleId();
        this.title = articleProjection.getTitle();
        this.content = articleProjection.getContent();
        this.authorName = articleProjection.getAuthorName();
        this.createDate = articleProjection.getCreateDate();
    }

}
