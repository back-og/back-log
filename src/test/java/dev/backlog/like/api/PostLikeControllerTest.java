package dev.backlog.like.api;

import com.epages.restdocs.apispec.Schema;
import dev.backlog.common.config.ControllerTestConfig;
import dev.backlog.like.dto.LikeStatusResponse;
import dev.backlog.like.service.PostLikeService;
import dev.backlog.user.dto.AuthInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostLikeController.class)
class PostLikeControllerTest extends ControllerTestConfig {

    @MockBean
    private PostLikeService postLikeService;

    @DisplayName("게시물 좋아요 요청이 오면 좋아요의 상태를 변경한 뒤 200 상태코드를 반환한다.")
    @Test
    void switchLikeTest() throws Exception {
        Long userId = 1L;
        Long postId = 1L;

        String token = TOKEN.substring(7);
        AuthInfo authInfo = new AuthInfo(userId, token);
        LikeStatusResponse response = new LikeStatusResponse(1, true);
        when(jwtTokenProvider.extractUserId(token)).thenReturn(userId);
        when(postLikeService.switchLike(postId, authInfo)).thenReturn(response);

        mockMvc.perform(put("/api/posts/{postId}/like", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TOKEN))
                .andDo(document("post-like",
                                resourceDetails().tag("Like").description("게시물 좋아요 요청")
                                        .responseSchema(Schema.schema("LikeStatusResponse")),
                                requestHeaders(
                                        headerWithName("Authorization").description("토큰")
                                ),
                                responseFields(
                                        fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                        fieldWithPath("like").type(JsonFieldType.BOOLEAN).description("좋아요 유무"))
                        )
                )
                .andExpect(jsonPath("likeCount").isNumber())
                .andExpect(jsonPath("like").isBoolean())
                .andExpect(status().isOk());
    }

}
