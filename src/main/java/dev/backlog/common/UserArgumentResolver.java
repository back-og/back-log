package dev.backlog.common;

import dev.backlog.domain.auth.model.oauth.JwtTokenProvider;
import dev.backlog.domain.user.dto.AuthInfo;
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
        return parameter.getParameterType().equals(AuthInfo.class);
    }

    @Override
    public AuthInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long userId = jwtTokenProvider.extractUserId(token);

            return new AuthInfo(userId, token);
        }
        throw new IllegalArgumentException("잘못된 권한 헤더입니다.");
    }

}
