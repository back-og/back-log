package dev.backlog.common.fixture;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.dto.SignupRequest;
import dev.backlog.auth.dto.AuthTokens;
import dev.backlog.post.dto.PostCreateRequest;
import dev.backlog.post.dto.PostUpdateRequest;
import dev.backlog.series.dto.SeriesCreateRequest;
import dev.backlog.series.dto.SeriesUpdateRequest;

import java.util.List;

public class DtoFixture {

    public static SignupRequest 회원_가입_정보() {
        return SignupRequest.of(
                "블로그 제목",
                "소개",
                "authCode",
                OAuthProvider.KAKAO
        );
    }

    public static AuthTokens 토큰_생성() {
        return AuthTokens.of(
                "accessToken",
                "refreshToken",
                "Bearer ",
                1000L
        );
    }

    public static PostCreateRequest 게시물_생성_요청() {
        return new PostCreateRequest(
                "시리즈",
                "제목", "내용",
                List.of("해쉬태그", "해쉬태그1"),
                "요약",
                true,
                "썸네일",
                "경로"
        );
    }

    public static PostUpdateRequest 게시물_수정_요청() {
        return new PostUpdateRequest(
                "변경된 시리즈",
                "변경된 제목",
                "변경된 내용",
                List.of("변경된 해쉬태그"),
                "변경된 요약",
                false,
                "변경된 URL",
                "변경된 경로"
        );
    }

    public static SeriesCreateRequest 시리즈_생성_요청() {
        return new SeriesCreateRequest(
                "시리즈 이름"
        );
    }

    public static SeriesUpdateRequest 시리즈_수정_요청() {
        return new SeriesUpdateRequest(
                "수정된 시리즈 이름"
        );
    }

}
