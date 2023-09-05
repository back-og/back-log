package dev.backlog.domain.user.api;

import com.epages.restdocs.apispec.Schema;
import dev.backlog.common.config.ControllerTestConfig;
import dev.backlog.domain.user.dto.UserDetailsResponse;
import dev.backlog.domain.user.dto.UserResponse;
import dev.backlog.domain.user.dto.UserUpdateRequest;
import dev.backlog.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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
    void findUserProfile() throws Exception {
        String nickname = "닉네임";
        UserResponse response = new UserResponse(
                "닉네임",
                "소개",
                "프로필이미지",
                "블로그제목"
        );

        when(userService.findUserProfile(nickname)).thenReturn(response);

        mockMvc.perform(get("/api/users/{nickname}", nickname)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("user-profile",
                                resourceDetails().tag("User").description("사용자 프로필 조회")
                                        .responseSchema(Schema.schema("UserResponse")),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
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
                .andExpect(jsonPath("$.nickname").value("닉네임"))
                .andExpect(jsonPath("$.introduction").value("소개"))
                .andExpect(jsonPath("$.profileImage").value("프로필이미지"))
                .andExpect(jsonPath("$.blogTitle").value("블로그제목")
                );
    }

    @DisplayName("로그인 되어있는 자신의 프로필을 조회할 수 있다.")
    @Test
    void findMyProfile() throws Exception {
        Long userId = 1000L;
        String token = "토큰";
        UserDetailsResponse response = new UserDetailsResponse(
                "닉네임",
                "소개",
                "프로필이미지",
                "블로그제목",
                "이메일"
        );

        when(jwtTokenProvider.extractUserId(token)).thenReturn(userId);
        when(userService.findMyProfile(userId)).thenReturn(response);

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("my-profile",
                                resourceDetails().tag("User").description("자신의 프로필 조회")
                                        .responseSchema(Schema.schema("UserDetailsResponse")),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),
                                        fieldWithPath("blogTitle").type(JsonFieldType.STRING).description("블로그 제목"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                                )
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("닉네임"))
                .andExpect(jsonPath("$.introduction").value("소개"))
                .andExpect(jsonPath("$.profileImage").value("프로필이미지"))
                .andExpect(jsonPath("$.blogTitle").value("블로그제목"))
                .andExpect(jsonPath("$.email").value("이메일")
                );
    }

    @DisplayName("로그인 되어있는 자신의 프로필을 수정할 수 있다.")
    @Test
    void updateUser() throws Exception {
        Long userId = 1000L;
        UserUpdateRequest request = new UserUpdateRequest(
                "새닉네임",
                "새이메일",
                "새프로필이미지",
                "새소개",
                "새블로그제목"
        );

        doNothing().when(userService).updateProfile(request, userId);

        mockMvc.perform(put("/api/users/me")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(document("user-update",
                                resourceDetails().tag("User").description("사용자 정보 수정")
                                        .requestSchema(Schema.schema("UserUpdateRequest")),
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

}
