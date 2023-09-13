package dev.backlog.series.service.query;

import dev.backlog.common.dto.SliceResponse;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.series.domain.Series;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.series.dto.SeriesSummaryResponse;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeriesQueryService {

    private static final int ZERO_POST_COUNT = 0;

    private final SeriesRepository seriesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public SliceResponse<SeriesSummaryResponse> findSeries(String nickname, Pageable pageable) {
        User user = userRepository.getByNickname(nickname);
        Slice<SeriesSummaryResponse> seriesSummaryResponses = seriesRepository.findAllByUser(user, pageable)
                .map(this::createSeriesSummaryResponse);
        return SliceResponse.from(seriesSummaryResponses);
    }

    private SeriesSummaryResponse createSeriesSummaryResponse(Series series) {
        List<Post> posts = postRepository.findAllBySeriesOrderByCreatedAt(series);
        if (posts.isEmpty()) {
            return SeriesSummaryResponse.of(series, ZERO_POST_COUNT);
        }
        Post firstPost = posts.get(0);
        int postCount = posts.size();
        return SeriesSummaryResponse.of(series, firstPost, postCount);
    }

}
