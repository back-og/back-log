package dev.backlog.post.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PostUpdateRequest(
        String series,
        @NotBlank
        String title,
        @NotBlank
        String content,
        List<String> hashtags,
        String summary,
        boolean isPublic,
        String thumbnailImage,
        @NotBlank
        String path
) {
}
