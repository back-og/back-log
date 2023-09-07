package dev.backlog.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends BackLogException {

    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
