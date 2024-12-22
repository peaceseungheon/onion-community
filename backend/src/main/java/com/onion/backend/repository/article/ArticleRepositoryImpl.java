package com.onion.backend.repository.article;

import static com.onion.backend.entity.QArticle.article;

import com.onion.backend.entity.Article;

import static com.onion.backend.entity.QBoard.board;

import com.onion.backend.repository.article.projections.ArticleProjection;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ArticleRepositoryImpl extends QuerydslRepositorySupport implements
    ArticleRepositoryCustom {

    public ArticleRepositoryImpl() {
        super(Article.class);
    }

    @Override
    public List<ArticleProjection> fetchOrderByCreateDateDesc(Long boardId) {
        return from(article)
            .select(Projections.constructor(ArticleProjection.class, article.board.id, article.id,
                article.title, article.content, article.author.name, article.createdAt))
            .where(article.board.id.eq(boardId))
            .orderBy(article.createdAt.desc())
            .limit(10)
            .fetch();
    }

    @Override
    public List<ArticleProjection> fetchArticleBefore(Long boardId, Long lastId) {
        return from(article)
            .select(Projections.constructor(ArticleProjection.class, board.id, article.id,
                article.title, article.content, article.author.name, article.createdAt))
            .join(article.board, board)
            .where(board.id.eq(boardId), article.id.lt(lastId))
            .orderBy(article.createdAt.desc())
            .fetch();
    }

    @Override
    public List<ArticleProjection> fetchArticleAfter(Long boardId, Long firstId) {
        return from(article)
            .select(Projections.constructor(ArticleProjection.class, board.id, article.id,
                article.title, article.content, article.author.name, article.createdAt))
            .join(article.board, board)
            .where(board.id.eq(boardId), article.id.gt(firstId))
            .orderBy(article.createdAt.desc())
            .fetch();
    }
}
