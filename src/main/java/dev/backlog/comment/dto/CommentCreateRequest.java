package dev.backlog.comment.dto;

import dev.backlog.comment.domain.Comment;
import dev.backlog.post.domain.Post;
import dev.backlog.user.domain.User;

public record CommentCreateRequest(String content, Long parentId) {

    public Comment toEntity(User user, Post post, Comment comment) {
        return Comment.builder()
                .content(content)
                .post(post)
                .writer(user)
                .parent(comment)
                .build();
    }

}
