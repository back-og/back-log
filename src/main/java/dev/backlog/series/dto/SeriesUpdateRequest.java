package dev.backlog.series.dto;

import jakarta.validation.constraints.NotBlank;

public record SeriesUpdateRequest(
        @NotBlank String seriesName
) {
}
