package dev.backlog.series.api;

import com.epages.restdocs.apispec.Schema;
import dev.backlog.common.config.ControllerTestConfig;
import dev.backlog.series.dto.SeriesCreateRequest;
import dev.backlog.series.dto.SeriesUpdateRequest;
import dev.backlog.series.service.SeriesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static dev.backlog.common.fixture.DtoFixture.시리즈생성요청;
import static dev.backlog.common.fixture.DtoFixture.시리즈수정요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeriesController.class)
class SeriesControllerTest extends ControllerTestConfig {

    @MockBean
    private SeriesService seriesService;

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

}
