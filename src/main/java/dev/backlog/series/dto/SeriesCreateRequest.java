package dev.backlog.series.dto;

import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;
import jakarta.validation.constraints.NotBlank;

public record SeriesCreateRequest(
        @NotBlank String seriesName
) {

    public Series toEntity(User user) {
        return Series.builder()
                .name(this.seriesName)
                .user(user)
                .build();
    }

}
