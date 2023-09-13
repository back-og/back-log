package dev.backlog.common.dto;

import org.springframework.data.domain.Slice;

import java.util.List;

public record SliceResponse<D>(int numberOfElements, boolean hasNext, List<D> data) {

    public static <T> SliceResponse<T> from(final Slice<T> posts) {
        return new SliceResponse<>(posts.getNumberOfElements(), posts.hasNext(), posts.getContent());
    }

}
