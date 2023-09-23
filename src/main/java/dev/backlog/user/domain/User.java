package dev.backlog.user.domain;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.common.entity.BaseEntity;
import dev.backlog.common.exception.DataLengthExceededException;
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
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.Objects;

import static dev.backlog.user.exception.UserErrorCode.INVALID_DATA_LENGTH;

@Entity
@Getter
@Where(clause = "is_deleted = false")
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    private static final String INVALID_NICKNAME_LENGTH_MESSAGE = "입력된 닉네임의 길이(%s)가 최대 길이(%s)를 넘겼습니다.";
    private static final String INVALID_INTRODUCTION_LENGTH_MESSAGE = "입력된 소개의 길이(%s)가 최대 길이(%s)를 넘겼습니다.";
    private static final String INVALID_BLOG_TITLE_LENGTH_MESSAGE = "입력된 제목의 길이(%s)가 최대 길이(%s)를 넘겼습니다.";

    private static final int MAX_NICKNAME_LENGTH = 20;
    private static final int MAX_INTRODUCTION_LENGTH = 100;
    private static final int MAX_BLOG_TITLE_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider oauthProvider;

    @Column(nullable = false)
    private String oauthProviderId;

    @Column(nullable = false, length = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Column(nullable = false)
    @Embedded
    private Email email;

    @Column(nullable = false)
    private String profileImage;

    @Column(length = MAX_INTRODUCTION_LENGTH)
    private String introduction;

    @Column(nullable = false, length = MAX_BLOG_TITLE_LENGTH)
    private String blogTitle;

    @Column(nullable = false)
    private LocalDate deletedDate;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    private User(
            OAuthProvider oauthProvider,
            String oauthProviderId,
            String nickname,
            Email email,
            String profileImage,
            String introduction,
            String blogTitle
    ) {
        validateNicknameLength(nickname);
        validateIntroductionLength(introduction);
        validateBlogTitleLength(blogTitle);
        this.oauthProvider = oauthProvider;
        this.oauthProviderId = oauthProviderId;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.introduction = introduction;
        this.blogTitle = blogTitle;
        this.deletedDate = LocalDate.of(9999, 12, 31);
        this.isDeleted = Boolean.FALSE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateNickName(String nickname) {
        validateNicknameLength(nickname);
        this.nickname = nickname;
    }

    public void updateEmail(String email) {
        this.email = new Email(email);
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateIntroduction(String introduction) {
        validateIntroductionLength(introduction);
        this.introduction = introduction;
    }

    public void updateBlogTitle(String blogTitle) {
        validateBlogTitleLength(blogTitle);
        this.blogTitle = blogTitle;
    }

    public void markUserAsDeleted() {
        this.isDeleted = true;
        this.deletedDate = LocalDate.now();
    }

    public void unmarkUserAsDeleted() {
        this.isDeleted = false;
        this.deletedDate = LocalDate.of(9999, 12, 31);
    }

    private void validateNicknameLength(String nickname) {
        if (nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new DataLengthExceededException(
                    INVALID_DATA_LENGTH,
                    String.format(INVALID_NICKNAME_LENGTH_MESSAGE, nickname.length(), MAX_NICKNAME_LENGTH)
            );
        }
    }

    private void validateIntroductionLength(String introduction) {
        if (introduction.length() > MAX_INTRODUCTION_LENGTH) {
            throw new DataLengthExceededException(
                    INVALID_DATA_LENGTH,
                    String.format(INVALID_INTRODUCTION_LENGTH_MESSAGE, introduction.length(), MAX_INTRODUCTION_LENGTH)
            );
        }
    }

    private void validateBlogTitleLength(String blogTitle) {
        if (blogTitle.length() > MAX_BLOG_TITLE_LENGTH) {
            throw new DataLengthExceededException(
                    INVALID_DATA_LENGTH,
                    String.format(INVALID_BLOG_TITLE_LENGTH_MESSAGE, blogTitle.length(), MAX_BLOG_TITLE_LENGTH)
            );
        }
    }

}
