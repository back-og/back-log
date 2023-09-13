package dev.backlog.auth.exception;

public class AuthErrorMessage {

    public final static String EXPIRED_TOKEN = "토큰이 만료되었습니다.";
    public final static String INVALID_FORMAT_TOKEN = "토큰의 형식이 적절하지 않습니다.";
    public final static String INVALID_STRUCTURE_TOKEN = "토큰이 올바르게 구성되지 않았거나, 적절하지 않게 수정되었습니다.";
    public final static String FAILED_SIGNATURE_VERIFICATION = "토큰의 서명 검증에 실패하였습니다.";
    public final static String EXPIRED_REFRESH_TOKEN = "리프레시 토큰이 만료되었습니다. 다시 로그인해 주세요.";

}
