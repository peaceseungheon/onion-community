package com.onion.backend.repository.article;

import com.onion.backend.repository.article.projections.ArticleProjection;
import java.util.List;

public interface ArticleRepositoryCustom {

    List<ArticleProjection> fetchOrderByCreateDateDesc(Long boardId);

}
