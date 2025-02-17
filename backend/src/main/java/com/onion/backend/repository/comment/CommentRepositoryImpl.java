package com.onion.backend.repository.comment;

import static com.onion.backend.entity.QComment.comment;

import com.onion.backend.entity.Comment;
import com.onion.backend.entity.QComment;
import com.onion.backend.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CommentRepositoryImpl extends QuerydslRepositorySupport implements CommentRepositoryCustom {

    public CommentRepositoryImpl() {
        super(Comment.class);
    }


    @Override
    public Comment getLatestCreateCommentByAuthor(User user) {
        return from(comment)
            .where(comment.user.eq(user))
            .orderBy(comment.createdAt.desc())
            .limit(1)
            .fetchOne();
    }

    @Override
    public List<Comment> getComments(Long articleId) {
        return from(comment)
            .where(comment.article.id.eq(articleId), comment.isDeleted.isFalse())
            .orderBy(comment.createdAt.desc())
            .fetch();
    }
}
