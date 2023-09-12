package dev.backlog.comment.dto;

import dev.backlog.comment.domain.Comment;
import dev.backlog.user.dto.Writer;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        Writer writer,
        String content,
        LocalDateTime createdAt
) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                Writer.from(comment.getWriter()),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }

}
