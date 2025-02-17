package com.onion.backend.repository.comment;

import com.onion.backend.entity.Comment;
import com.onion.backend.entity.User;
import java.util.List;

public interface CommentRepositoryCustom {

    Comment getLatestCreateCommentByAuthor(User user);
    List<Comment> getComments(Long articleId);

}
