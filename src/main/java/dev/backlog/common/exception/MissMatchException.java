package dev.backlog.common.exception;

import lombok.Getter;

@Getter
public class MissMatchException extends BackLogException {

    private final ErrorCode errorCode;

    public MissMatchException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
