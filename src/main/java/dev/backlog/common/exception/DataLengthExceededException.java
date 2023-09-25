package dev.backlog.common.exception;

import lombok.Getter;

@Getter
public class DataLengthExceededException extends BackLogException {

    private final ErrorCode errorCode;

    public DataLengthExceededException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
