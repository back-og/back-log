package dev.backlog.common.advice;

import dev.backlog.common.exception.ErrorCode;

public record ErrorResponse(int status, ErrorCode errorCode) {
}
