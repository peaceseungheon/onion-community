package com.onion.backend.repository.comment;

import com.onion.backend.entity.Comment;
import com.onion.backend.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Optional<Comment> findByUser(User user);

}
