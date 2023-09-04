package dev.backlog.domain.post.infrastructure.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private static final QPost post = new QPost("post");

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Post> findLikedPostsByTimePeriod(String timePeriod, Pageable pageable) {
        List<Post> postList = jpaQueryFactory
                .selectFrom(post)
                .where(getDateCondition(timePeriod))
                .orderBy(post.likes.size().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1l)
                .fetch();

        List<Post> content = getContent(postList);
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private List<Post> getContent(List<Post> postList) {
        List<Post> content = new ArrayList<>();
        for (Post post : postList) {
            content.add(post);
        }
        return content;
    }

    private BooleanExpression getDateCondition(String timePeriod) {
        LocalDate fromDate;
        LocalDate toDate = LocalDate.now();
        fromDate = getFromDate(timePeriod, toDate);
        return post.createdAt.between(fromDate.atStartOfDay(), toDate.plusDays(1).atStartOfDay());
    }

    private LocalDate getFromDate(String timePeriod, LocalDate toDate) {
        LocalDate fromDate;
        switch (timePeriod) {
            case "today" -> fromDate = toDate;
            case "week" -> fromDate = toDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
            case "month" -> fromDate = toDate.withDayOfMonth(1);
            case "year" -> fromDate = toDate.withDayOfYear(1);
            default -> fromDate = LocalDate.of(1970, 1, 1);
        }
        return fromDate;
    }

}
