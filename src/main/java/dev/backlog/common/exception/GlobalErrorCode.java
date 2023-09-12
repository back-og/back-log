package dev.backlog.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    VALIDATION_ERROR("G000", "유효하지 않은 입력입니다."),
    ;

    private final String code;
    private final String message;

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }

}
