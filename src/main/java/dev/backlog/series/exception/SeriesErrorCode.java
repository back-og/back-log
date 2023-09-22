package dev.backlog.series.exception;

import dev.backlog.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeriesErrorCode implements ErrorCode {
    SERIES_NOT_FOUNT("S004", "시리즈를 찾을 수 없습니다."),
    INVALID_DATA_LENGTH("S022", "시리즈에서 처리할 수 있는 데이터 크기를 초과했습니다."),
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

