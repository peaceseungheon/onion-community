package com.onion.backend.repository.article;

import com.onion.backend.entity.Article;
import com.onion.backend.entity.User;
import com.onion.backend.repository.article.projections.ArticleProjection;
import java.util.List;

public interface ArticleRepositoryCustom {

    Article getByArticleId(Long articleId);

    List<ArticleProjection> fetchOrderByCreateDateDesc(Long boardId);

    List<ArticleProjection> fetchArticleBefore(Long boardId, Long lastId);
    List<ArticleProjection> fetchArticleAfter(Long boardId, Long firstId);

    Article getLatestCreateArticleByAuthor(User user);

    Article getLatestUpdateArticleByAuthor(User user);
}
