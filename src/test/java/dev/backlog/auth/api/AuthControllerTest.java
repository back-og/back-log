package dev.backlog.auth.api;

import com.epages.restdocs.apispec.Schema;
import dev.backlog.auth.AuthTokens;
import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.dto.SignupRequest;
import dev.backlog.auth.service.OAuthService;
import dev.backlog.common.config.ControllerTestConfig;
import dev.backlog.common.fixture.DtoFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static dev.backlog.common.fixture.DtoFixture.토큰생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTestConfig {

    @MockBean
    private OAuthService oAuthService;

    @DisplayName("사용자가 로그인 버튼을 누르면 해당하는 서비스의 접근 권한 url로 리다이렉트한다.")
    @Test
    void redirectAuthCodeRequestUrlTest() throws Exception {
        String expectedRedirectUrl = "https://example.com";

        when(oAuthService.getAuthCodeRequestUrl(eq(OAuthProvider.KAKAO))).thenReturn(expectedRedirectUrl);

        mockMvc.perform(get("/api/auth/v2/{oAuthProvider}", OAuthProvider.KAKAO)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("auth-redirect",
                                resourceDetails().tag("Auth").description("접근 권한 url 리다이렉트"),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("oAuthProvider").description("OAuth 로그인 타입")
                                ),
                                responseHeaders(
                                        headerWithName("Location").description("리다이렉션 대상 URL")
                                )
                        )
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedRedirectUrl));
    }

    @DisplayName("올바른 로그인 타입과 인증 코드, 추가 정보를 받아 회원가입에 성공한다.")
    @Test
    void signupTest() throws Exception {
        SignupRequest signupRequest = DtoFixture.회원가입정보();
        AuthTokens expectedTokens = 토큰생성();

        when(oAuthService.signup(signupRequest)).thenReturn(expectedTokens);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/v2/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest))
                )
                .andDo(document("auth-signup",
                                resourceDetails().tags("Auth").description("회원가입")
                                        .requestSchema(Schema.schema("SignupRequest"))
                                        .responseSchema(Schema.schema("AuthTokens")),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("blogTitle").type(JsonFieldType.STRING).description("블로그 제목"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개"),
                                        fieldWithPath("oAuthProvider").type(JsonFieldType.STRING).description("로그인 타입"),
                                        fieldWithPath("authCode").type(JsonFieldType.STRING).description("인증 코드")
                                ),
                                responseFields(
                                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
                                        fieldWithPath("grantType").type(JsonFieldType.STRING).description("Bearer 타입"),
                                        fieldWithPath("expiresIn").type(JsonFieldType.NUMBER).description("토큰 만료 시간(초)")
                                )
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.grantType").value("Bearer "))
                .andExpect(jsonPath("$.expiresIn").value(1000L)
                );
    }

    @DisplayName("올바른 로그인 타입과 인증 코드를 받아 로그인에 성공한다.")
    @Test
    void loginTest() throws Exception {
        AuthTokens expectedTokens = 토큰생성();

        when(oAuthService.login(any(), any())).thenReturn(expectedTokens);

        mockMvc.perform(get("/api/auth/v2/login/{oAuthProvider}", OAuthProvider.KAKAO)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("code", "authCode")
                )
                .andDo(document("auth-login",
                                resourceDetails().tag("Auth").description("로그인")
                                        .responseSchema(Schema.schema("AuthTokens")),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("oAuthProvider").description("OAuth 로그인 타입")
                                ),
                                queryParameters(
                                        parameterWithName("code").description("인증 코드")
                                ),
                                responseFields(
                                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
                                        fieldWithPath("grantType").type(JsonFieldType.STRING).description("Bearer 타입"),
                                        fieldWithPath("expiresIn").type(JsonFieldType.NUMBER).description("토큰 만료 시간(초)")
                                )
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.grantType").value("Bearer "))
                .andExpect(jsonPath("$.expiresIn").value(1000L));
    }

    @DisplayName("리프레시 토큰이 만료되지 않았을 경우 리프레시 토큰으로 액세스 토큰을 갱신한다.")
    @Test
    void updateAccessToken() throws Exception {
        Long userId = 1000L;
        String token = "토큰";
        String refreshToken = "refreshToken";
        AuthTokens newAuthTokens = AuthTokens.of(
                "newGeneratedAccessToken",
                refreshToken,
                "Bearer ",
                1000L);

        when(jwtTokenProvider.extractUserId(token)).thenReturn(userId);
        when(oAuthService.renew(anyLong(), any())).thenReturn(newAuthTokens);

        mockMvc.perform(post("/api/auth/v2/renew-token")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("renew-token",
                                resourceDetails().tag("Auth").description("토큰 갱신")
                                        .responseSchema(Schema.schema("AuthTokens")),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
                                        fieldWithPath("grantType").type(JsonFieldType.STRING).description("Bearer 타입"),
                                        fieldWithPath("expiresIn").type(JsonFieldType.NUMBER).description("토큰 만료 시간(초)")
                                )
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newGeneratedAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken))
                .andExpect(jsonPath("$.grantType").value("Bearer "))
                .andExpect(jsonPath("$.expiresIn").value(1000L)
                );
    }

}
