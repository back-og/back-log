package dev.backlog.post.dto;

import dev.backlog.post.domain.Post;
import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostCreateRequest(
        String series,
        @NotBlank
        String title,
        @NotBlank
        String content,
        List<String> hashtags,
        String summary,
        @NotNull
        boolean isPublic,
        String thumbnailImage,
        @NotBlank
        String path
) {

    public Post toEntity(Series series, User user) {
        return Post.builder()
                .series(series)
                .title(this.title)
                .user(user)
                .content(this.content)
                .summary(this.summary)
                .isPublic(this.isPublic)
                .thumbnailImage(this.thumbnailImage)
                .path(this.path)
                .build();
    }

}
