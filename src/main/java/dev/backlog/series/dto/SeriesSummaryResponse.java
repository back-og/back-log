package dev.backlog.series.dto;

import dev.backlog.post.domain.Post;
import dev.backlog.series.domain.Series;

import java.time.LocalDateTime;

public record SeriesSummaryResponse(
        Long seriesId,
        String thumbnailImage,
        String name,
        int postCount,
        LocalDateTime updatedAt
) {

    public static SeriesSummaryResponse of(final Series series, final Post firstPost, final int postCount) {
        return new SeriesSummaryResponse(
                series.getId(),
                firstPost.getThumbnailImage(),
                series.getName(),
                postCount,
                series.getUpdatedAt()
        );
    }

    public static SeriesSummaryResponse of(final Series series, final int postCount) {
        return new SeriesSummaryResponse(
                series.getId(),
                null,
                series.getName(),
                postCount,
                series.getUpdatedAt()
        );
    }

}
