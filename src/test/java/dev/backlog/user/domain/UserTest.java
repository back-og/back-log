package dev.backlog.user.domain;

import dev.backlog.common.exception.DataLengthExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = 유저();
    }

    @Test
    void updateNickNameTest() {
        String updatedNickname = "업데이트닉네임";
        user.updateNickName(updatedNickname);

        assertThat(user.getNickname()).isEqualTo(updatedNickname);
    }

    @Test
    void updateEmailTest() {
        String updatedEmail = "업데이트이메일";
        user.updateEmail(updatedEmail);

        assertThat(user.getEmail().getEmail()).isEqualTo(updatedEmail);
    }

    @Test
    void updateProfileImageTest() {
        String updatedProfileImage = "업데이트프로필사진";
        user.updateProfileImage(updatedProfileImage);

        assertThat(user.getProfileImage()).isEqualTo(updatedProfileImage);
    }

    @Test
    void updateIntroductionTest() {
        String updatedIntroduction = "업데이트소개";
        user.updateIntroduction(updatedIntroduction);

        assertThat(user.getIntroduction()).isEqualTo(updatedIntroduction);
    }

    @Test
    void updateBlogTitleTest() {
        String updatedBlogTitle = "업데이트블로그제목";
        user.updateBlogTitle(updatedBlogTitle);

        assertThat(user.getBlogTitle()).isEqualTo(updatedBlogTitle);
    }

    @Test
    void markUserAsDeletedTest() {
        user.markUserAsDeleted();

        assertThat(user.isDeleted()).isTrue();
        assertThat(user.getDeletedDate()).isNotEqualTo(LocalDate.of(9999, 12, 31));
    }

    @Test
    void unmarkUserAsDeletedTest() {
        user.unmarkUserAsDeleted();

        assertThat(user.isDeleted()).isFalse();
        assertThat(user.getDeletedDate()).isEqualTo(LocalDate.of(9999, 12, 31));
    }

    @Test
    void validateNicknameLengthTest() {
        String updatedNickname = "abcdefghijklmnopqrstuvwzyz";

        assertThatThrownBy(() -> user.updateNickName(updatedNickname))
                .isInstanceOf(DataLengthExceededException.class);
    }

    @Test
    void validateIntroductionLength() {
        String updatedIntroduction = "abcdefghijklmnopqrstuvwzyzabcdefghijklmnopqrstuvwzyzabcdefghijklmnopqrstuvwzyzabcdefghijklmnopqrstuvwzyzabcdefghijklmnopqrstuvwzyzabcdefghijklmnopqrstuvwzyz";

        assertThatThrownBy(() -> user.updateIntroduction(updatedIntroduction))
                .isInstanceOf(DataLengthExceededException.class);
    }

    @Test
    void validateBlogTitleLengthTest() {
        String updatedBlogTitle = "abcdefghijklmnopqrstuvwzyzabcdefghijklmnopqrstuvwzyz";

        assertThatThrownBy(() -> user.updateBlogTitle(updatedBlogTitle))
                .isInstanceOf(DataLengthExceededException.class);
    }

}
