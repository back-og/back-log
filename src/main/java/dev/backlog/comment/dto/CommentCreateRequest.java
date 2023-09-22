package dev.backlog.comment.dto;

import dev.backlog.comment.domain.Comment;
import dev.backlog.post.domain.Post;
import dev.backlog.user.domain.User;
import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @NotBlank
        String content,
        @NotBlank
        Long parentId
) {

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .content(content)
                .post(post)
                .writer(user)
                .build();
    }

}
