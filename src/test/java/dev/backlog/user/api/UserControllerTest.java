package dev.backlog.user.api;

import com.epages.restdocs.apispec.Schema;
import dev.backlog.common.config.ControllerTestConfig;
import dev.backlog.user.domain.Email;
import dev.backlog.user.dto.UserDetailsResponse;
import dev.backlog.user.dto.UserResponse;
import dev.backlog.user.dto.UserUpdateRequest;
import dev.backlog.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest extends ControllerTestConfig {

    @MockBean
    private UserService userService;

    @DisplayName("사용자의 프로필을 조회할 수 있다.")
    @Test
    void findUserProfileTest() throws Exception {
        String nickname = "닉네임";
        UserResponse response = getUserResponse();

        when(userService.findUserProfile(nickname)).thenReturn(response);

        mockMvc.perform(get("/api/users/v1/{nickname}", nickname)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("user-profile",
                                resourceDetails().tag("User").description("사용자 프로필 조회")
                                        .responseSchema(Schema.schema("UserResponse")),
                                pathParameters(
                                        parameterWithName("nickname").description("닉네임")
                                ),
                                responseFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),
                                        fieldWithPath("blogTitle").type(JsonFieldType.STRING).description("블로그 제목")
                                )
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(response.nickname()))
                .andExpect(jsonPath("$.introduction").value(response.introduction()))
                .andExpect(jsonPath("$.profileImage").value(response.profileImage()))
                .andExpect(jsonPath("$.blogTitle").value(response.blogTitle()));
    }

    @DisplayName("로그인 되어있는 자신의 프로필을 조회할 수 있다.")
    @Test
    void findMyProfileTest() throws Exception {
        Long userId = 1000L;
        UserDetailsResponse response = getUserDetailsResponse();

        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);
        when(userService.findMyProfile(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/users/v1/me")
                        .header("Authorization", TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("my-profile",
                                resourceDetails().tag("User").description("자신의 프로필 조회")
                                        .responseSchema(Schema.schema("UserDetailsResponse")),
                                requestHeaders(
                                        headerWithName("Authorization").description("토큰")
                                ),
                                responseFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),
                                        fieldWithPath("blogTitle").type(JsonFieldType.STRING).description("블로그 제목"),
                                        fieldWithPath("email.email").type(JsonFieldType.STRING).description("이메일")
                                )
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(response.nickname()))
                .andExpect(jsonPath("$.introduction").value(response.introduction()))
                .andExpect(jsonPath("$.profileImage").value(response.profileImage()))
                .andExpect(jsonPath("$.blogTitle").value(response.blogTitle()))
                .andExpect(jsonPath("$.email.email").value(response.email().getEmail()));
    }

    @DisplayName("로그인 되어있는 자신의 프로필을 수정할 수 있다.")
    @Test
    void updateUserTest() throws Exception {
        Long userId = 1000L;
        UserUpdateRequest request = getUserUpdateRequest();

        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);

        mockMvc.perform(put("/api/users/v1/me")
                        .header("Authorization", TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(document("user-update",
                                resourceDetails().tag("User").description("사용자 정보 수정")
                                        .requestSchema(Schema.schema("UserUpdateRequest")),
                                requestHeaders(
                                        headerWithName("Authorization").description("토큰")
                                ),
                                requestFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),
                                        fieldWithPath("blogTitle").type(JsonFieldType.STRING).description("블로그 제목"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                                )
                        )
                )
                .andExpect(status().isNoContent()
                );
    }

    @DisplayName("사용자는 탈퇴할 수 있다.")
    @Test
    void deleteUserTest() throws Exception {
        Long userId = 1000L;

        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);

        mockMvc.perform(delete("/api/users/v1/me")
                        .header("Authorization", TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("user-delete",
                                resourceDetails().tag("User").description("사용자 탈퇴"),
                                requestHeaders(
                                        headerWithName("Authorization").description("토큰")
                                )
                        )
                )
                .andExpect(status().isOk()
                );
    }

    private UserUpdateRequest getUserUpdateRequest() {
        return new UserUpdateRequest(
                "새닉네임",
                "programmers@naver.com",
                "새프로필이미지",
                "새소개",
                "새블로그제목"
        );
    }

    private UserResponse getUserResponse() {
        return new UserResponse(
                "닉네임",
                "소개",
                "프로필이미지",
                "블로그제목"
        );
    }

    private UserDetailsResponse getUserDetailsResponse() {
        return new UserDetailsResponse(
                "닉네임",
                "소개",
                "프로필이미지",
                "블로그제목",
                new Email("이메일")
        );
    }

}
