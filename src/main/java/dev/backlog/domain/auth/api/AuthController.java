package dev.backlog.domain.auth.api;

import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoLoginRequest;
import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoSignUpRequest;
import dev.backlog.domain.auth.model.AuthTokens;
import dev.backlog.domain.auth.model.oauth.dto.CreateAccessTokenRequest;
import dev.backlog.domain.auth.model.oauth.dto.LogoutRequest;
import dev.backlog.domain.auth.service.OAuthLoginService;
import dev.backlog.domain.auth.service.OAuthLogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final OAuthLoginService oAuthLoginService;
    private final OAuthLogoutService oAuthLogoutService;

    @PostMapping("/signup/kakao")
    public ResponseEntity<AuthTokens> kakaoSignUp(@RequestBody KakaoSignUpRequest params) {
        return ResponseEntity.ok(oAuthLoginService.kakaoSignUp(params));
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<AuthTokens> kakaoLogin(@RequestBody KakaoLoginRequest params) {
        return ResponseEntity.ok(oAuthLoginService.kakaoLogin(params));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthTokens> refreshAccessToken(@RequestBody CreateAccessTokenRequest request) {
        AuthTokens newTokens = oAuthLoginService.refreshAccessToken(request.refreshToken());

        return ResponseEntity.ok(newTokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> kakaoLogout(@RequestBody LogoutRequest params) {
        oAuthLogoutService.kakaoLogout(params.accessToken());

        return ResponseEntity.ok().build();
    }

}
