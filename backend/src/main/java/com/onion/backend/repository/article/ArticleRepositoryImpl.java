package com.onion.backend.repository.article;

import static com.onion.backend.entity.QArticle.article;

import com.onion.backend.entity.Article;
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
}
