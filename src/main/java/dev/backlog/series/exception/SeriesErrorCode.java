package dev.backlog.series.exception;

import dev.backlog.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeriesErrorCode implements ErrorCode {
    SERIES_NOT_FOUNT("S004", "시리즈를 찾을 수 없음"),
    ;

    private final String errorCode;
    private final String message;

    @Override
    public String code() {
        return null;
    }

    @Override
    public String message() {
        return null;
    }

}

