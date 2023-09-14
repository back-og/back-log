package dev.backlog.comment.dto;

import dev.backlog.comment.domain.Comment;
import dev.backlog.post.domain.Post;
import dev.backlog.user.domain.User;

public record CommentCreateRequest(String content) {

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .content(content)
                .post(post)
                .writer(user)
                .build();
    }

}
