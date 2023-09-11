package dev.backlog.domain.series.dto;

import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.model.User;
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
