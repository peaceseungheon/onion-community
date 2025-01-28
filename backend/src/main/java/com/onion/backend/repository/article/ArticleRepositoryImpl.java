package com.onion.backend.repository.article;

import static com.onion.backend.entity.QArticle.article;

import com.onion.backend.entity.Article;

import static com.onion.backend.entity.QBoard.board;

import com.onion.backend.entity.User;
import com.onion.backend.exception.ResourceNotFoundException;
import com.onion.backend.repository.article.projections.ArticleProjection;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ArticleRepositoryImpl extends QuerydslRepositorySupport implements
    ArticleRepositoryCustom {

    public ArticleRepositoryImpl() {
        super(Article.class);
    }

    @Override
    public Article getByArticleId(Long articleId) {
        return Optional.ofNullable(
            from(article)
                .where(article.id.eq(articleId))
                .fetchOne()
        ).orElseThrow(() -> new ResourceNotFoundException("게시글 정보를 조회하지 못했습니다."));
    }

    @Override
    public List<ArticleProjection> fetchOrderByCreateDateDesc(Long boardId) {
        return from(article)
            .select(Projections.constructor(ArticleProjection.class, article.board.id, article.id,
                article.title, article.content, article.author.name, article.author.id,
                article.createdAt))
            .where(article.board.id.eq(boardId), article.isDeleted.isFalse())
            .orderBy(article.createdAt.desc())
            .limit(10)
            .fetch();
    }

    @Override
    public List<ArticleProjection> fetchArticleBefore(Long boardId, Long lastId) {
        return from(article)
            .select(Projections.constructor(ArticleProjection.class, board.id, article.id,
                article.title, article.content, article.author.name, article.author.id,
                article.createdAt))
            .join(article.board, board)
            .where(board.id.eq(boardId), article.id.lt(lastId), article.isDeleted.isFalse())
            .orderBy(article.createdAt.desc())
            .fetch();
    }

    @Override
    public List<ArticleProjection> fetchArticleAfter(Long boardId, Long firstId) {
        return from(article)
            .select(Projections.constructor(ArticleProjection.class, board.id, article.id,
                article.title, article.content, article.author.name, article.author.id,
                article.createdAt))
            .join(article.board, board)
            .where(board.id.eq(boardId), article.id.gt(firstId), article.isDeleted.isFalse())
            .orderBy(article.createdAt.desc())
            .fetch();
    }

    @Override
    public Article getLatestCreateArticleByAuthor(User user) {
        return from(article)
            .where(article.author.eq(user))
            .orderBy(article.createdAt.desc())
            .limit(1)
            .fetchOne();
    }

    @Override
    public Article getLatestUpdateArticleByAuthor(User user) {
        return from(article)
            .where(article.author.eq(user))
            .orderBy(article.updatedAt.desc())
            .limit(1)
            .fetchOne();
    }
}
