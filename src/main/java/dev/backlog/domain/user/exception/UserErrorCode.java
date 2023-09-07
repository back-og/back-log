package dev.backlog.domain.user.exception;

import dev.backlog.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    U004("유저를 찾을 수 없음"),
    ;

    private final String message;

}
