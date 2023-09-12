package dev.backlog.series.api;

import dev.backlog.series.dto.SeriesCreateRequest;
import dev.backlog.series.service.SeriesService;
import dev.backlog.user.dto.AuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @PostMapping("/v1")
    public ResponseEntity<Void> create(@Valid @RequestBody SeriesCreateRequest seriesCreateRequest, AuthInfo authInfo) {
        seriesService.create(seriesCreateRequest, authInfo);
        return ResponseEntity.noContent().build();
    }

}
