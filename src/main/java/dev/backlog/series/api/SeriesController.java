package dev.backlog.series.api;

import dev.backlog.common.annotation.Login;
import dev.backlog.common.dto.SliceResponse;
import dev.backlog.series.dto.SeriesCreateRequest;
import dev.backlog.series.dto.SeriesSummaryResponse;
import dev.backlog.series.dto.SeriesUpdateRequest;
import dev.backlog.series.service.SeriesService;
import dev.backlog.series.service.query.SeriesQueryService;
import dev.backlog.user.dto.AuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/series/v1")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;
    private final SeriesQueryService seriesQueryService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SeriesCreateRequest seriesCreateRequest,
                                       @Login AuthInfo authInfo) {
        seriesService.create(seriesCreateRequest, authInfo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<SliceResponse<SeriesSummaryResponse>> findSeries(String nickname,
                                                                           @PageableDefault(size = 30, sort = "updatedAt", direction = DESC) Pageable pageable) {
        SliceResponse<SeriesSummaryResponse> series = seriesQueryService.findSeries(nickname, pageable);
        return ResponseEntity.ok(series);
    }

    @PutMapping("/{seriesId}")
    public ResponseEntity<Void> updateSeries(@Login AuthInfo authInfo,
                                             @PathVariable Long seriesId,
                                             @Valid @RequestBody SeriesUpdateRequest seriesUpdateRequest) {
        seriesService.updateSeries(authInfo, seriesId, seriesUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{seriesId}")
    public ResponseEntity<Void> deleteSeries(@Login AuthInfo authInfo,
                                             @PathVariable Long seriesId) {
        seriesService.deleteSeries(authInfo, seriesId);
        return ResponseEntity.noContent().build();
    }

}
