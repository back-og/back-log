package dev.backlog.post.dto;

import jakarta.validation.constraints.NotBlank;

public record SeriesPostsFindRequest(
        @NotBlank
        String nickname,
        @NotBlank
        String series
) {
}
