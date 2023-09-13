package dev.backlog.series.dto;

import dev.backlog.series.domain.Series;

public record SeriesResponse(
        Long seriesId,
        String seriesName
) {

    public static SeriesResponse from(Series series) {
        return new SeriesResponse(
                series.getId(),
                series.getName()
        );
    }

}
