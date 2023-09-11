package dev.backlog.domain.user.exception;

import dev.backlog.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUNT("U004", "유저를 찾을 수 없음 userId = %s"),
    ;

    private final String errorCode;
    private final String message;

}
