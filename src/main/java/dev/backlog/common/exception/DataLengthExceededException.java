package dev.backlog.common.exception;

public class DataLengthExceededException extends BackLogException {

    private final ErrorCode errorCode;

    public DataLengthExceededException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
