package dev.backlog.post.infra.jpa.query;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.QHashtag;
import dev.backlog.post.domain.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static dev.backlog.post.domain.QPost.post;
import static dev.backlog.post.domain.QPostHashtag.postHashtag;
import static dev.backlog.user.domain.QUser.user;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryDslRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Post> findLikedPostsByTimePeriod(String timePeriod, Pageable pageable) {
        List<Post> postList = jpaQueryFactory
                .selectFrom(post)
                .where(getDateCondition(timePeriod))
                .orderBy(post.likes.size().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1L)
                .fetch();

        List<Post> content = getContent(postList);
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<Post> findByUserNicknameAndHashtag(String nickName, String hashtag, Pageable pageable) {
        int limit = pageable.getPageSize() + 1;
        List<Post> posts = jpaQueryFactory
                .selectDistinct(post)
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(postHashtag).on(postHashtag.post.id.eq(post.id))
                .fetchJoin()
                .where(
                        isNicknameCondition(nickName),
                        isHashtagCondition(hashtag)
                )
                .offset(pageable.getOffset())
                .limit(limit)
                .orderBy(getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .fetch();

        boolean hasNext = determineHasNext(posts, pageable.getPageSize());

        return new SliceImpl<>(posts, pageable, hasNext);
    }

    private boolean determineHasNext(List<Post> posts, int pageSize) {
        if (posts.size() > pageSize) {
            posts.remove(pageSize);
            return true;
        }
        return false;
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        return sort.stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    String property = order.getProperty();
                    PathBuilder orderByExpression = new PathBuilder<>(Post.class, "post");
                    return new OrderSpecifier<>(direction, orderByExpression.get(property));
                })
                .toList();
    }

    private BooleanExpression isNicknameCondition(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return null;
        }
        return post.user.nickname.eq(nickname);
    }

    private BooleanExpression isHashtagCondition(String hashtag) {
        if (hashtag == null || hashtag.isEmpty()) {
            return null;
        }
        return QHashtag.hashtag.name.eq(hashtag);
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
