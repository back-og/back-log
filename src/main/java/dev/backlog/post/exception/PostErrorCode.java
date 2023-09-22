package dev.backlog.post.exception;

import dev.backlog.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {
    POST_NOT_FOUND("P004", "게시글을 찾을 수 없습니다."),
    INVALID_DATA_LENGTH("P022", "처리할 수 있는 데이터 크기를 초과했습니다."),
    ;

    private final String errorCode;
    private final String message;

    @Override
    public String code() {
        return errorCode;
    }

    @Override
    public String message() {
        return message;
    }

}
