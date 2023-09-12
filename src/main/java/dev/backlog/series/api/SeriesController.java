package dev.backlog.series.api;

import dev.backlog.series.dto.SeriesCreateRequest;
import dev.backlog.series.dto.SeriesUpdateRequest;
import dev.backlog.series.service.SeriesService;
import dev.backlog.user.dto.AuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @PostMapping("/v1")
    public ResponseEntity<Void> create(@Valid @RequestBody SeriesCreateRequest seriesCreateRequest,
                                       AuthInfo authInfo) {
        seriesService.create(seriesCreateRequest, authInfo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/v1/{seriesId}")
    public ResponseEntity<Void> updateSeries(AuthInfo authInfo,
                                             @PathVariable Long seriesId,
                                             @Valid @RequestBody SeriesUpdateRequest seriesUpdateRequest) {
        seriesService.updateSeries(authInfo, seriesId, seriesUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/v1/{seriesId}")
    public ResponseEntity<Void> deleteSeries(AuthInfo authInfo,
                                             @PathVariable Long seriesId) {
        seriesService.deleteSeries(authInfo, seriesId);
        return ResponseEntity.noContent().build();
    }

}
