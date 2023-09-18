package dev.backlog.common.fixture;

import dev.backlog.comment.domain.Comment;
import dev.backlog.like.domain.PostLike;
import dev.backlog.post.domain.Hashtag;
import dev.backlog.post.domain.Post;
import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.Email;
import dev.backlog.user.domain.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.backlog.auth.domain.oauth.OAuthProvider.KAKAO;

public class EntityFixture {

    public static User 유저1() {
        return User.builder()
                .oauthProvider(KAKAO)
                .oauthProviderId("KAKAO_ID")
                .nickname("닉네임")
                .email(new Email("이메일"))
                .profileImage("프로필사진URL")
                .introduction("소개글")
                .blogTitle("블로그 이름")
                .build();
    }

    public static Post 공개_게시물(User user, Series series) {
        return Post.builder()
                .series(series)
                .user(user)
                .title("제목")
                .content("내용")
                .summary("요약")
                .isPublic(true)
                .thumbnailImage("썸네일URL")
                .path("경로")
                .build();
    }

    public static Post 비공개_게시물(User user, Series series) {
        return Post.builder()
                .series(series)
                .user(user)
                .title("제목")
                .content("내용")
                .summary("요약")
                .isPublic(false)
                .thumbnailImage("썸네일URL")
                .path("경로")
                .build();
    }

    public static List<Post> 게시물_모음(User user, Series series) {
        List<Post> posts = new ArrayList<>();
        for (int index = 0; index < 30; index++) {
            Post post = 공개_게시물(user, series);
            posts.add(post);
        }
        return posts;
    }

    public static Comment 댓글1(User user, Post post) {
        return Comment.builder()
                .writer(user)
                .post(post)
                .content("내용")
                .isDeleted(false)
                .build();
    }

    public static List<Comment> 댓글_모음(User user, Post post) {
        List<Comment> comments = new ArrayList<>();
        for (int index = 0; index < 5; index++) {
            Comment comment = 댓글1(user, post);
            comments.add(comment);
        }
        return comments;
    }

    public static Series 시리즈1(User user) {
        return Series.builder()
                .user(user)
                .name("시리즈")
                .build();
    }

    public static List<Series> 시리즈_모음(User user) {
        List<Series> series = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            series.add(시리즈1(user));
        }
        return series;
    }

    public static PostLike 좋아요1(User user, Post post) {
        return PostLike.builder()
                .post(post)
                .user(user)
                .build();
    }

    public static List<Hashtag> 해쉬태그_모음() {
        return createHashtags("해쉬태그", "해쉬태그1", "해쉬태그2", "해쉬태그3");
    }

    private static List<Hashtag> createHashtags(String... 해쉬태그) {
        return Arrays.stream(해쉬태그)
                .map(Hashtag::new)
                .toList();
    }

}
