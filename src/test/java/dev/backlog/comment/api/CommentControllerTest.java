package dev.backlog.comment.api;

import com.epages.restdocs.apispec.Schema;
import dev.backlog.comment.dto.CreateCommentRequest;
import dev.backlog.comment.service.CommentService;
import dev.backlog.common.config.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;


import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends ControllerTestConfig {

    @MockBean
    private CommentService commentService;

    @DisplayName("입력받은 게시물 아이디와 사용자 아이디에 대해 댓글을 생성할 수 있다.")
    @Test
    void createTest() throws Exception {
        Long userId = 1L;
        Long postId = 2L;
        Long commentId = 3L;

        CreateCommentRequest request = new CreateCommentRequest("댓글테스트댓글테스트");
        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);
        when(commentService.create(eq(request), any(), any()))
                .thenReturn(commentId);

        mockMvc.perform(post("/api/comments/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TOKEN)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(document("comment-create",
                                resourceDetails().tag("댓글").description("댓글 생성")
                                        .requestSchema(Schema.schema("CreateCommentRequest")),
                                pathParameters(
                                        parameterWithName("postId").description("게시물 식별자")
                                ),
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

}
