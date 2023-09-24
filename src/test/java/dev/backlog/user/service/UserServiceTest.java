package dev.backlog.user.service;

import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.UserDetailsResponse;
import dev.backlog.user.dto.UserResponse;
import dev.backlog.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User 유저1;

    @BeforeEach
    void setUp() {
        유저1 = 유저();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findUserProfileTest() {
        String nickname = "닉네임";
        when(userRepository.getByNickname(유저1.getNickname())).thenReturn(유저1);

        UserResponse userProfile = userService.findUserProfile(nickname);

        assertThat(userProfile.nickname()).isEqualTo(nickname);
    }

    @Test
    void findMyProfileTest() {
        Long userId = 유저1.getId();

        when(userRepository.getById(유저1.getId())).thenReturn(유저1);

        UserDetailsResponse myProfile = userService.findMyProfile(userId);

        assertAll(
                () -> assertThat(myProfile.nickname()).isEqualTo(유저1.getNickname()),
                () -> assertThat(myProfile.profileImage()).isEqualTo(유저1.getProfileImage()),
                () -> assertThat(myProfile.blogTitle()).isEqualTo(유저1.getBlogTitle()),
                () -> assertThat(myProfile.introduction()).isEqualTo(유저1.getIntroduction()),
                () -> assertThat(myProfile.email()).isEqualTo(유저1.getEmail())
        );
    }

    @Test
    void updateUserTest() {
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                "새닉네임",
                "새이메일",
                "새이미지",
                "새소개",
                "새블로그제목"
        );

        when(userRepository.getById(유저1.getId())).thenReturn(유저1);

        userService.updateProfile(updateRequest, 유저1.getId());
        User updatedUser = userRepository.getById(유저1.getId());

        assertAll(
                () -> assertThat(updatedUser.getNickname()).isEqualTo(updateRequest.nickname()),
                () -> assertThat(updatedUser.getBlogTitle()).isEqualTo(updateRequest.blogTitle()),
                () -> assertThat(updatedUser.getProfileImage()).isEqualTo(updateRequest.profileImage())
        );
    }

}
