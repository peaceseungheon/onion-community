package com.onion.backend.dto;

import com.onion.backend.entity.Comment;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CommentOut {

    private String author;
    private String content;
    private String createdAt;
    private String updatedAt;

    public CommentOut(Comment comment) {
        this.author = comment.getUser().getName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updatedAt = comment.getUpdatedAt()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
