package dev.backlog.domain.post.model;

import dev.backlog.common.entity.BaseEntity;
import dev.backlog.domain.like.model.Like;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.model.User;
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

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    private static final int NEGATIVE_NUMBER = 0;
    private static final long INITIAL_VIEW_COUNT = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(length = 100)
    private String summary;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column
    private String thumbnailImage;

    @Column(nullable = false)
    private String path;

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

    public void removeAllHashtag() {
        this.postHashtags.clear();
    }

    public void increaseViewCount() {
        this.viewCount = viewCount + 1;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateSummary(String summary) {
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

}
