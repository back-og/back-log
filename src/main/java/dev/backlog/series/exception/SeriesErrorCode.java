package dev.backlog.series.exception;

import dev.backlog.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeriesErrorCode implements ErrorCode {
    SERIES_NOT_FOUNT("S004"),
    ;

    private final String errorCode;

}

