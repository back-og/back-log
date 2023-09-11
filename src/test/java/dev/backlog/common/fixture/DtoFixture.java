package dev.backlog.common.fixture;

import dev.backlog.domain.auth.AuthTokens;
import dev.backlog.domain.auth.model.oauth.OAuthProvider;
import dev.backlog.domain.auth.model.oauth.dto.SignupRequest;
import dev.backlog.domain.post.dto.PostCreateRequest;
import dev.backlog.domain.post.dto.PostUpdateRequest;
import dev.backlog.domain.series.dto.SeriesCreateRequest;

import java.util.List;

public class DtoFixture {

    public static SignupRequest 회원가입정보() {
        return SignupRequest.of(
                "블로그 제목",
                "소개",
                "authCode",
                OAuthProvider.KAKAO
        );
    }

    public static AuthTokens 토큰생성() {
        return AuthTokens.of(
                "accessToken",
                "refreshToken",
                "Bearer ",
                1000L
        );
    }

    public static PostCreateRequest 게시물생성요청() {
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

    public static PostUpdateRequest 게시물수정요청() {
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

    public static SeriesCreateRequest 시리즈생성요청() {
        return new SeriesCreateRequest(
                "시리즈 이름"
        );
    }

}
