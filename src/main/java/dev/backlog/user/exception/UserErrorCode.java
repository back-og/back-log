package dev.backlog.user.exception;

import dev.backlog.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("U004", "유저를 찾을 수 없습니다."),
    INVALID_DATA_LENGTH("U022", "처리할 수 있는 데이터 크기를 초과했습니다."),
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
