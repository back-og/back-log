package dev.backlog.series.api;

import com.epages.restdocs.apispec.Schema;
import dev.backlog.common.config.ControllerTestConfig;
import dev.backlog.common.dto.SliceResponse;
import dev.backlog.series.dto.SeriesCreateRequest;
import dev.backlog.series.dto.SeriesSummaryResponse;
import dev.backlog.series.dto.SeriesUpdateRequest;
import dev.backlog.series.service.SeriesService;
import dev.backlog.series.service.query.SeriesQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static dev.backlog.common.fixture.DtoFixture.시리즈생성요청;
import static dev.backlog.common.fixture.DtoFixture.시리즈수정요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeriesController.class)
class SeriesControllerTest extends ControllerTestConfig {

    @MockBean
    private SeriesService seriesService;

    @MockBean
    private SeriesQueryService seriesQueryService;

    @DisplayName("시리즈를 생성할 수 있다.")
    @Test
    void createTest() throws Exception {
        Long userId = 1L;
        Long seriesId = 1L;
        SeriesCreateRequest seriesCreateRequest = 시리즈생성요청();
        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);
        when(seriesService.create(eq(seriesCreateRequest), any())).thenReturn(seriesId);

        mockMvc.perform(post("/api/series/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TOKEN)
                        .content(objectMapper.writeValueAsString(seriesCreateRequest)))
                .andDo(document("series-create",
                                resourceDetails().tag("시리즈").description("시리즈 생성 요청")
                                        .requestSchema(Schema.schema("SeriesCreateRequest")),
                                requestHeaders(
                                        headerWithName("Authorization").description("토큰")
                                ),
                                requestFields(
                                        fieldWithPath("seriesName").type(JsonFieldType.STRING).description("시리즈 이름")
                                )
                        )
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("시리즈 목록을 최신 순서로 조회할 수 있다.")
    @Test
    void findSeries() throws Exception {
        //given
        final long seriesId = 1l;
        final long userId = 1l;
        SliceResponse<SeriesSummaryResponse> sliceResponse = getSeriesSliceResponse(seriesId);

        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);
        when(seriesQueryService.findSeries(any(), any(PageRequest.class))).thenReturn(sliceResponse);

        //when, then
        mockMvc.perform(get("/api/series/v1")
                        .param("nickname", "닉네임")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(30))
                        .param("sort", "updatedAt,desc")
                        .header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("series-find",
                                resourceDetails().tag("시리즈").description("시리즈 목록 조회")
                                        .responseSchema(Schema.schema("SliceResponse")),
                                queryParameters(
                                        parameterWithName("nickname").description("사용자 닉네임"),
                                        parameterWithName("page").description("현재 페이지"),
                                        parameterWithName("size").description("페이지 당 게시물 수"),
                                        parameterWithName("sort").description("정렬 기준")
                                ),
                                responseFields(
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("시리즈 수"),
                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("마지막 페이지 체크"),
                                        fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("시리즈 데이터"),
                                        fieldWithPath("data[].seriesId").type(JsonFieldType.NUMBER).description("시리즈 번호"),
                                        fieldWithPath("data[].thumbnailImage").type(JsonFieldType.STRING).description("섬네일 이미지"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("시리즈 이름"),
                                        fieldWithPath("data[].postCount").type(JsonFieldType.NUMBER).description("시리즈 안의 게시물 수"),
                                        fieldWithPath("data[].updatedAt").type(JsonFieldType.STRING).description("시리즈 수정 시간")
                                )
                        )
                );
    }

    @DisplayName("시리즈를 수정할 수 있다.")
    @Test
    void updateSeriesTest() throws Exception {
        final Long seriesId = 1L;
        final Long userId = 1L;
        SeriesUpdateRequest seriesUpdateRequest = 시리즈수정요청();
        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);

        mockMvc.perform(put("/api/series/v1/{seriesId}", seriesId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TOKEN)
                        .content(objectMapper.writeValueAsString(seriesUpdateRequest)))
                .andDo(document("series-update",
                                resourceDetails().tag("시리즈").description("시리즈 수정 요청")
                                        .requestSchema(Schema.schema("SeriesUpdateRequest")),
                                pathParameters(parameterWithName("seriesId").description("시리즈 식별자")),
                                requestFields(
                                        fieldWithPath("seriesName").type(JsonFieldType.STRING).description("시리즈 이름")
                                )
                        )
                ).andExpect(status().isNoContent());
    }

    @DisplayName("시리즈를 삭제할 수 있다.")
    @Test
    void deleteSeriesTest() throws Exception {
        final Long seriesId = 1L;
        final Long userId = 1L;
        when(jwtTokenProvider.extractUserId(TOKEN)).thenReturn(userId);

        mockMvc.perform(delete("/api/series/v1/{seriesId}", seriesId)
                        .header("Authorization", TOKEN)
                )
                .andDo(document("series-delete",
                                resourceDetails().tag("시리즈").description("시리즈 삭제 요청"),
                                pathParameters(parameterWithName("seriesId").description("시리즈 식별자")
                                )
                        )
                ).andExpect(status().isNoContent());
    }

    private SliceResponse<SeriesSummaryResponse> getSeriesSliceResponse(long seriesId) {
        return new SliceResponse<>(
                1,
                false,
                List.of(new SeriesSummaryResponse(
                                seriesId,
                                "썸네일 이미지",
                                "시리즈 이름",
                                10,
                                LocalDateTime.now()
                        )
                )
        );
    }

}
