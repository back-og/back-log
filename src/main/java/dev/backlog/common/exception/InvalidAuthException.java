package dev.backlog.common.exception;

import lombok.Getter;

@Getter
public class InvalidAuthException extends BackLogException {

    private final ErrorCode errorCode;

    public InvalidAuthException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
