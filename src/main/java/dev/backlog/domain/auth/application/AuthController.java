package dev.backlog.domain.auth.application;

import dev.backlog.domain.auth.infrastructure.kakao.KakaoLoginParams;
import dev.backlog.domain.auth.infrastructure.kakao.KakaoSignUpParams;
import dev.backlog.domain.auth.model.AuthTokens;
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

    @PostMapping("/signup/kakao")
    public ResponseEntity<AuthTokens> kakaoSignUp(@RequestBody KakaoSignUpParams params) {
        return ResponseEntity.ok(oAuthLoginService.kakaoSignUp(params));
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<AuthTokens> kakaoLogin(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.kakaoLogin(params));
    }
}
