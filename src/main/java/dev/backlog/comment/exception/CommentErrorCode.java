package dev.backlog.comment.exception;

import dev.backlog.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    INVALID_WRITER("C000", "해당 댓글의 작성자가 아닙니다."),
    COMMENT_NOT_FOUND("C004", "댓글을 찾을 수 없습니다."),
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
