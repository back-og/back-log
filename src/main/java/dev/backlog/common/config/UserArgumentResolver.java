package dev.backlog.common.config;

import dev.backlog.auth.domain.oauth.JwtTokenProvider;
import dev.backlog.common.annotation.Login;
import dev.backlog.user.dto.AuthInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthInfo.class) && parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public AuthInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = httpServletRequest.getHeader("Authorization");

        Login login = parameter.getParameterAnnotation(Login.class);

        if (login.required()) {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                Long userId = jwtTokenProvider.extractUserId(token);
                return new AuthInfo(userId, token);
            }
            throw new IllegalArgumentException("잘못된 권한 헤더입니다.");
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long userId = jwtTokenProvider.extractUserId(token);
            return new AuthInfo(userId, token);
        }
        return null;
    }

}
