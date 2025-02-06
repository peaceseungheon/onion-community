package com.onion.backend.repository.comment;

import com.onion.backend.entity.Comment;
import com.onion.backend.entity.User;

public interface CommentRepositoryCustom {

    Comment getLatestCreateCommentByAuthor(User user);

}
