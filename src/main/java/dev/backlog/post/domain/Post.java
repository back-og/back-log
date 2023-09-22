package dev.backlog.post.domain;

import dev.backlog.common.entity.BaseEntity;
import dev.backlog.common.exception.DataLengthExceededException;
import dev.backlog.like.domain.PostLike;
import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static dev.backlog.post.exception.PostErrorCode.INVALID_DATA_LENGTH;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    private static final String INVALID_TITLE_LENGTH_MESSAGE = "입력된 제목의 길이(%s)가 최대 길이(%s)를 넘겼습니다.";
    private static final String INVALID_CONTENT_LENGTH_MESSAGE = "입력된 본문의 길이(%s)가 최대 길이(%s)를 넘겼습니다.";
    private static final String INVALID_SUMMARY_LENGTH_MESSAGE = "입력된 요약의 길이(%s)가 최대 길이(%s)를 넘겼습니다.";

    private static final int MAX_CONTENT_LENGTH = 5000;
    private static final int MAX_SUMMARY_LENGTH = 100;
    private static final long INITIAL_VIEW_COUNT = 0L;
    private static final int MAX_TITLE_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @Column(nullable = false, length = MAX_TITLE_LENGTH)
    private String title;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false, length = MAX_CONTENT_LENGTH)
    private String content;

    @Column(length = MAX_SUMMARY_LENGTH)
    private String summary;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column
    private String thumbnailImage;

    @Column(nullable = false)
    private String path;

    private int likeCount = 0;

    @Builder
    private Post(
            Series series,
            User user,
            String title,
            String content,
            String summary,
            Boolean isPublic,
            String thumbnailImage,
            String path
    ) {
        validateTitleLength(title);
        validateContentLength(content);
        validateSummaryLength(summary);
        this.series = series;
        this.user = user;
        this.title = title;
        this.viewCount = INITIAL_VIEW_COUNT;
        this.content = content;
        this.summary = summary;
        this.isPublic = isPublic;
        this.thumbnailImage = thumbnailImage;
        this.path = path;
    }

    public void verifyPostOwner(User user) {
        if (!this.user.equals(user)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }

    public void addAllPostHashtag(List<PostHashtag> postHashtags) {
        this.postHashtags.clear();
        this.postHashtags.addAll(postHashtags);
    }

    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    public void decreaseLikeCount() {
        this.likeCount -= 1;
    }


    public void removeAllHashtag() {
        this.postHashtags.clear();
    }

    public void increaseViewCount() {
        this.viewCount = viewCount + 1;
    }

    public void updateTitle(String title) {
        validateTitleLength(title);
        this.title = title;
    }

    public void updateContent(String content) {
        validateContentLength(content);
        this.content = content;
    }

    public void updateSummary(String summary) {
        validateSummaryLength(summary);
        this.summary = summary;
    }

    public void updateIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void updateThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public void updatePath(String path) {
        this.path = path;
    }

    public void updateSeries(Series series) {
        this.series = series;
    }

    private void validateTitleLength(String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new DataLengthExceededException(
                    INVALID_DATA_LENGTH,
                    String.format(INVALID_TITLE_LENGTH_MESSAGE, title.length(), MAX_TITLE_LENGTH));
        }
    }

    private void validateContentLength(String content) {
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new DataLengthExceededException(
                    INVALID_DATA_LENGTH,
                    String.format(INVALID_CONTENT_LENGTH_MESSAGE, content.length(), MAX_CONTENT_LENGTH));
        }
    }

    private void validateSummaryLength(String summary) {
        if (summary.length() > MAX_SUMMARY_LENGTH) {
            throw new DataLengthExceededException(
                    INVALID_DATA_LENGTH,
                    String.format(INVALID_SUMMARY_LENGTH_MESSAGE, summary.length(), MAX_SUMMARY_LENGTH));
        }
    }

}
