package com.onion.backend.dto;


import com.onion.backend.entity.Article;
import com.onion.backend.entity.Comment;
import com.onion.backend.repository.article.projections.ArticleProjection;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
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
    private Long authorNo;
    private String createDate;
    private List<CommentOut> comments;

    public ArticleDto(ArticleProjection articleProjection){
        this.boardId = articleProjection.getBoardId();
        this.articleId = articleProjection.getArticleId();
        this.title = articleProjection.getTitle();
        this.content = articleProjection.getContent();
        this.authorName = articleProjection.getAuthorName();
        this.authorNo = articleProjection.getAuthorNo();
        this.createDate = articleProjection.getCreateDate();
    }

    public ArticleDto(Article article){
        this.boardId = article.getBoard().getId();
        this.articleId = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.authorName = article.getAuthor().getName();
        this.authorNo = article.getAuthor().getId();
        this.createDate = article.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ArticleDto(Article article, List<Comment> comments){
        this(article);
        this.comments = comments.stream().map(CommentOut::new).collect(Collectors.toList());
    }

}
