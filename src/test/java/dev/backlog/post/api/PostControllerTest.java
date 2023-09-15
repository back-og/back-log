package dev.backlog.post.api;

import com.epages.restdocs.apispec.Schema;
import dev.backlog.comment.dto.CommentResponse;
import dev.backlog.common.config.ControllerTestConfig;
import dev.backlog.common.dto.SliceResponse;
import dev.backlog.common.fixture.DtoFixture;
import dev.backlog.post.dto.PostCreateRequest;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.post.dto.PostSummaryResponse;
import dev.backlog.post.dto.PostUpdateRequest;
import dev.backlog.post.service.PostService;
import dev.backlog.post.service.query.PostQueryService;
import dev.backlog.series.dto.SeriesResponse;
import dev.backlog.user.dto.Writer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest extends ControllerTestConfig {

    @MockBean
    private PostService postService;

    @MockBean
    private PostQueryService postQueryService;

    @DisplayName("게시물 생성 요청을 받아 처리 후 201 코드를 반환하고 게시물 조회 URI를 반환한다.")
    @Test
    void createTest() throws Exception {
        Long userId = 1L;
        Long postId = 2L;
        PostCreateRequest request = DtoFixture.게시물생성요청();
        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);
        when(postService.create(eq(request), any()))
                .thenReturn(postId);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TOKEN)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(document("post-create",
                                resourceDetails().tag("게시물").description("게시물 생성 요청")
                                        .requestSchema(Schema.schema("PostCreateRequest")),
                                requestHeaders(
                                        headerWithName("Authorization").description("토큰")
                                ),
                                requestFields(
                                        fieldWithPath("series").type(JsonFieldType.STRING).description("시리즈"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("hashtags").type(JsonFieldType.ARRAY).description("해시태그"),
                                        fieldWithPath("summary").type(JsonFieldType.STRING).description("요약"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("공개 여부"),
                                        fieldWithPath("thumbnailImage").type(JsonFieldType.STRING).description("썸네일 URL"),
                                        fieldWithPath("path").type(JsonFieldType.STRING).description("게시물 경로")
                                ),
                                responseHeaders(
                                        headerWithName("Location").description("Created Post RedirectURL")
                                )
                        )
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/posts/" + postId));
    }

    @DisplayName("게시글을 상세 조회할 수 있다.")
    @Test
    void findPostTest() throws Exception {
        //given
        final long postId = 1l;
        final long seriesId = 1l;
        final long userId = 1l;
        final long commentId = 1l;
        PostResponse postResponse = getPostResponse(postId, seriesId, userId, commentId);

        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);
        when(postQueryService.findPostById(any(), any())).thenReturn(postResponse);

        //when, then
        mockMvc.perform(get("/api/posts/{postId}", postId)
                        .header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("post-find",
                                resourceDetails().tag("게시물").description("게시물 상세 조회")
                                        .responseSchema(Schema.schema("PostResponse")),
                                pathParameters(parameterWithName("postId").description("게시물 식별자")),
                                responseFields(
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 번호"),
                                        fieldWithPath("series").type(JsonFieldType.OBJECT).description("시리즈"),
                                        fieldWithPath("series.seriesId").type(JsonFieldType.NUMBER).description("시리즈 번호"),
                                        fieldWithPath("series.seriesName").type(JsonFieldType.STRING).description("시리즈 이름"),
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("게시글 작성자 번호"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("게시글 조회수"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("summary").type(JsonFieldType.STRING).description("게시글 요약"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("게시글 공개 여부"),
                                        fieldWithPath("path").type(JsonFieldType.STRING).description("게시글 저장 경로"),
                                        fieldWithPath("createdAt").type(JsonFieldType.NULL).description("게시글 작성 시간"),
                                        fieldWithPath("comments[]").type(JsonFieldType.ARRAY).description("댓글"),
                                        fieldWithPath("comments[].commentId").type(JsonFieldType.NUMBER).description("댓글 작성자 번호"),
                                        fieldWithPath("comments[].writer").type(JsonFieldType.OBJECT).description("댓글 작성자"),
                                        fieldWithPath("comments[].writer.userId").type(JsonFieldType.NUMBER).description("댓글 작성자 번호"),
                                        fieldWithPath("comments[].writer.nickname").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
                                        fieldWithPath("comments[].content").type(JsonFieldType.STRING).description("댓글 본문"),
                                        fieldWithPath("comments[].createdAt").type(JsonFieldType.NULL).description("댓글 작성 시간")
                                )
                        )
                );
    }

    @DisplayName("사용자가 좋아요를 누른 게시글 목록을 최신 순서로 반환한다.")
    @Test
    void findLikedPostsTest() throws Exception {
        //given
        final long postId = 1l;
        final long userId = 1l;
        SliceResponse<PostSummaryResponse> sliceResponse = getPostSliceResponse(postId, userId);

        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);
        when(postQueryService.findLikedPostsByUser(any(), any(PageRequest.class))).thenReturn(sliceResponse);

        //when, then
        mockMvc.perform(get("/api/posts/like")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(30))
                        .param("sort", "createdAt,asc")
                        .header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("posts-find-like",
                                resourceDetails().tag("게시물").description("좋아요 누른 게시물 조회")
                                        .responseSchema(Schema.schema("SliceResponse")),
                                queryParameters(
                                        parameterWithName("page").description("현재 페이지"),
                                        parameterWithName("size").description("페이지 당 게시물 수"),
                                        parameterWithName("sort").description("정렬 기준")
                                ),
                                responseFields(
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("게시글 수"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("마지막 페이지 체크"),
                                        fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("게시글 데이터"),
                                        fieldWithPath("data[].postId").type(JsonFieldType.NUMBER).description("게시글 번호"),
                                        fieldWithPath("data[].thumbnailImage").type(JsonFieldType.STRING).description("시리즈"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("시리즈 번호"),
                                        fieldWithPath("data[].summary").type(JsonFieldType.STRING).description("시리즈 이름"),
                                        fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("게시글 작성자 번호"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.NULL).description("게시글 작성 시간"),
                                        fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글"),
                                        fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("댓글 작성자 번호")
                                )
                        )
                );
    }

    @DisplayName("사용자 닉네임과 시리즈 이름으로 게시글 목록을 과거순으로 반환한다.")
    @Test
    void findSeriesPostsTest() throws Exception {
        //given
        final long postId = 1l;
        final long userId = 1l;
        SliceResponse<PostSummaryResponse> sliceResponse = getPostSliceResponse(postId, userId);
        when(postQueryService.findPostsByUserAndSeries(any(), any(String.class), any(PageRequest.class))).thenReturn(sliceResponse);

        //when, then
        mockMvc.perform(get("/api/posts/series")
                        .param("series", "시리즈")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(30))
                        .param("sort", "createdAt,asc"))
                .andExpect(status().isOk())
                .andDo(document("posts-find-series",
                                resourceDetails().tag("게시물").description("시리즈별 게시물 조회")
                                        .responseSchema(Schema.schema("SliceResponse")),
                                queryParameters(
                                        parameterWithName("series").description("시리즈 이름"),
                                        parameterWithName("page").description("현재 페이지"),
                                        parameterWithName("size").description("페이지 당 게시물 수"),
                                        parameterWithName("sort").description("정렬 기준")
                                ),
                                responseFields(
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("게시글 수"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("마지막 페이지 체크"),
                                        fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("게시글 데이터"),
                                        fieldWithPath("data[].postId").type(JsonFieldType.NUMBER).description("게시글 번호"),
                                        fieldWithPath("data[].thumbnailImage").type(JsonFieldType.STRING).description("시리즈"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("시리즈 번호"),
                                        fieldWithPath("data[].summary").type(JsonFieldType.STRING).description("시리즈 이름"),
                                        fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("게시글 작성자 번호"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.NULL).description("게시글 작성 시간"),
                                        fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글"),
                                        fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("댓글 작성자 번호")
                                )
                        )
                );
    }

    @DisplayName("게시물 목록을 최신 순서로 조회한다.")
    @Test
    void findRecentPostsTest() throws Exception {
        //given
        final long postId = 1l;
        final long userId = 1l;
        SliceResponse<PostSummaryResponse> sliceResponse = getPostSliceResponse(postId, userId);
        when(postQueryService.findPostsInLatestOrder(any(PageRequest.class))).thenReturn(sliceResponse);

        //when, then
        mockMvc.perform(get("/api/posts/recent")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(30))
                        .param("sort", "createdAt,desc")
                        .header("AuthorizationCode", "tmp"))
                .andExpect(status().isOk())
                .andDo(document("posts-find-recent",
                                resourceDetails().tag("게시물").description("게시물 최근 조회")
                                        .responseSchema(Schema.schema("SliceResponse")),
                                queryParameters(
                                        parameterWithName("page").description("현재 페이지"),
                                        parameterWithName("size").description("페이지 당 게시물 수"),
                                        parameterWithName("sort").description("정렬 기준")
                                ),
                                responseFields(
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("게시글 수"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("마지막 페이지 체크"),
                                        fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("게시글 데이터"),
                                        fieldWithPath("data[].postId").type(JsonFieldType.NUMBER).description("게시글 번호"),
                                        fieldWithPath("data[].thumbnailImage").type(JsonFieldType.STRING).description("시리즈"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("시리즈 번호"),
                                        fieldWithPath("data[].summary").type(JsonFieldType.STRING).description("시리즈 이름"),
                                        fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("게시글 작성자 번호"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.NULL).description("게시글 작성 시간"),
                                        fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글"),
                                        fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("댓글 작성자 번호")
                                )
                        )
                );
    }

    @DisplayName("좋아요 많이 받은 순서로 게시물을 조회한다.")
    @Test
    void findTrendPostsTest() throws Exception {
        //given
        final long postId = 1l;
        final long userId = 1l;
        SliceResponse<PostSummaryResponse> sliceResponse = getPostSliceResponse(postId, userId);
        when(postQueryService.findLikedPosts(anyString(), any(PageRequest.class))).thenReturn(sliceResponse);

        //when, then
        String defaultTimePeriod = "week";
        mockMvc.perform(get("/api/posts/trend")
                        .param("timePeriod", defaultTimePeriod)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(30))
                        .header("AuthorizationCode", "tmp"))
                .andExpect(status().isOk())
                .andDo(document("posts-find-trend",
                                resourceDetails().tag("게시물").description("게시물 트렌딩 조회")
                                        .responseSchema(Schema.schema("SliceResponse")),
                                queryParameters(
                                        parameterWithName("timePeriod").description("today, week, month, year 필터링 조건"),
                                        parameterWithName("page").description("현재 페이지"),
                                        parameterWithName("size").description("페이지 당 게시물 수")
                                ),
                                responseFields(
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("게시글 수"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("마지막 페이지 체크"),
                                        fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("게시글 데이터"),
                                        fieldWithPath("data[].postId").type(JsonFieldType.NUMBER).description("게시글 번호"),
                                        fieldWithPath("data[].thumbnailImage").type(JsonFieldType.STRING).description("시리즈"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("시리즈 번호"),
                                        fieldWithPath("data[].summary").type(JsonFieldType.STRING).description("시리즈 이름"),
                                        fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("게시글 작성자 번호"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.NULL).description("게시글 작성 시간"),
                                        fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글"),
                                        fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("댓글 작성자 번호")
                                )
                        )
                );
    }

    @DisplayName("사용자의 게시물 업데이트 요청을 받아 업데이트 한 뒤 204 상태코드를 반환한다.")
    @Test
    void updatePostTest() throws Exception {
        final Long postId = 1L;
        final Long userId = 1L;
        PostUpdateRequest request = DtoFixture.게시물수정요청();
        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);

        mockMvc.perform(put("/api/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TOKEN)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(document("post-update",
                                resourceDetails().tag("게시물").description("게시물 업데이트")
                                        .requestSchema(Schema.schema("PostUpdateRequest")),
                                pathParameters(parameterWithName("postId").description("게시물 식별자")),
                                requestFields(
                                        fieldWithPath("series").type(JsonFieldType.STRING).description("시리즈"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("hashtags").type(JsonFieldType.ARRAY).description("해시태그"),
                                        fieldWithPath("summary").type(JsonFieldType.STRING).description("요약"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("공개 여부"),
                                        fieldWithPath("thumbnailImage").type(JsonFieldType.STRING).description("썸네일 URL"),
                                        fieldWithPath("path").type(JsonFieldType.STRING).description("게시물 경로"))
                        )
                ).andExpect(status().isNoContent());
    }

    @DisplayName("사용자 닉네임으로 게시물리스트를 조회후 상태코드 200과 함께 데이터를 리턴한다.")
    @Test
    void searchByUserNicknameTest() throws Exception {
        final long postId = 1l;
        final long userId = 1l;
        SliceResponse<PostSummaryResponse> sliceResponse = getPostSliceResponse(postId, userId);

        when(postQueryService.searchByNicknameAndHashtag(any(), any(), any())).thenReturn(sliceResponse);

        mockMvc.perform(get("/api/posts/search")
                        .param("hashtag", "tag")
                        .param("nickname", "nickname")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("posts-find-nickname&hashtag",
                                resourceDetails().tag("게시물").description("게시물 필터 조회")
                                        .responseSchema(Schema.schema("SliceResponse")),
                                queryParameters(
                                        parameterWithName("nickname").description("유저 닉네임").optional(),
                                        parameterWithName("hashtag").description("해쉬태").optional(),
                                        parameterWithName("timePeriod").description("today, week, month, year 필터링 조건").optional(),
                                        parameterWithName("page").description("현재 페이지").optional(),
                                        parameterWithName("size").description("페이지 당 게시물 수").optional()
                                ),
                                responseFields(
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("게시글 수"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("마지막 페이지 체크"),
                                        fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("게시글 데이터"),
                                        fieldWithPath("data[].postId").type(JsonFieldType.NUMBER).description("게시글 번호"),
                                        fieldWithPath("data[].thumbnailImage").type(JsonFieldType.STRING).description("시리즈"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("시리즈 번호"),
                                        fieldWithPath("data[].summary").type(JsonFieldType.STRING).description("시리즈 이름"),
                                        fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("게시글 작성자 번호"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.NULL).description("게시글 작성 시간"),
                                        fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글"),
                                        fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("댓글 작성자 번호")
                                )
                        )
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(sliceResponse.numberOfElements()))
                .andExpect(jsonPath("$.hasNext").value(false));
    }

    @DisplayName("게시물 삭제 요청이 들어오면 삭제 후 204 상태코드를 반환한다.")
    @Test
    void deletePostTest() throws Exception {
        long postId = 1L;

        mockMvc.perform(delete("/api/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TOKEN))
                .andDo(document("post-delete",
                                resourceDetails().tag("게시물").description("게시물 업데이트"),
                                pathParameters(
                                        parameterWithName("postId").description("게시물 식별자")
                                )
                        )
                )
                .andExpect(status().isNoContent());
    }

    private PostResponse getPostResponse(
            long postId,
            long seriesId,
            long userId,
            long commentId) {
        return new PostResponse(
                postId,
                new SeriesResponse(seriesId, "시리즈"),
                userId,
                "제목",
                0l,
                "내용",
                "요약",
                true,
                "경로",
                null,
                List.of(new CommentResponse(
                        commentId,
                        new Writer(userId, "닉네임"),
                        "내용", null)
                )
        );
    }

    private SliceResponse<PostSummaryResponse> getPostSliceResponse(long postId, long userId) {
        return new SliceResponse<>(
                10,
                false,
                List.of(new PostSummaryResponse(
                        postId,
                        "썸네일 이미지",
                        "제목",
                        "요약",
                        userId,
                        null,
                        0,
                        0)
                )
        );
    }

}
