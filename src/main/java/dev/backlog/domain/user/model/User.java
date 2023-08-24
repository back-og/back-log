package dev.backlog.domain.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private static final String DEFAULT_BLOG_TITLE = ".log";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider oauthProvider;

    @Column(nullable = false)
    private String oauthProviderId;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
    @Embedded
    private Email email;

    @Column(nullable = false)
    private String profileImage;

    @Column(length = 100)
    private String introduction;

    @Column(nullable = false, length = 20)
    private String blogTitle;

    @Column(nullable = false)
    private LocalDate deletedDate;

    @Builder
    private User(OAuthProvider oauthProvider,
                 String oauthProviderId,
                 String nickname, Email email,
                 String profileImage,
                 String introduction,
                 String blogTitle
    ) {
        this.oauthProvider = oauthProvider;
        this.oauthProviderId = oauthProviderId;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.introduction = introduction;
        this.blogTitle = blogTitle;
        this.deletedDate = LocalDate.MAX;
    }

    public static String checkBlogTitle(String blogTitle, String nickname) {
        if (Objects.isNull(blogTitle) || blogTitle.isEmpty()) {
            return nickname + DEFAULT_BLOG_TITLE;
        }
        return blogTitle;
    }

}
