package dev.backlog.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    VALIDATION_ERROR("G000", "잘못된 요청"),
    ;

    private final String errorCode;
    private final String message;

}