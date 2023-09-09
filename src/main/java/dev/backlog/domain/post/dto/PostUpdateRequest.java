package dev.backlog.domain.post.dto;

import java.util.List;

public record PostUpdateRequest(
        String series,
        String title,
        String content,
        List<String> hashtags,
        String summary,
        boolean isPublic,
        String thumbnailImage,
        String path
) {
}
