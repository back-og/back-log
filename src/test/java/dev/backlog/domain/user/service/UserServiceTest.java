package dev.backlog.domain.user.service;

import dev.backlog.domain.auth.model.oauth.JwtTokenProvider;
import dev.backlog.domain.user.dto.UserDetailsResponse;
import dev.backlog.domain.user.dto.UserResponse;
import dev.backlog.domain.user.dto.UserUpdateRequest;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.backlog.common.fixture.EntityFixture.유저1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        유저1 = 유저1();
    }

    @Test
    void findUserProfileTest() {
        String nickname = "닉네임";
        when(userRepository.findByNickname(유저1.getNickname())).thenReturn(Optional.of(유저1));

        UserResponse userProfile = userService.findUserProfile(nickname);

        assertThat(userProfile.nickname()).isEqualTo(nickname);
    }

    @Test
    void findMyProfileTest() {
        Long userId = 유저1.getId();

        when(userRepository.findById(유저1.getId())).thenReturn(Optional.of(유저1));

        UserDetailsResponse myProfile = userService.findMyProfile(userId);

        assertAll(
                () -> assertThat(myProfile.nickname()).isEqualTo(유저1.getNickname()),
                () -> assertThat(myProfile.profileImage()).isEqualTo(유저1.getProfileImage()),
                () -> assertThat(myProfile.blogTitle()).isEqualTo(유저1.getBlogTitle()),
                () -> assertThat(myProfile.introduction()).isEqualTo(유저1.getIntroduction()),
                () -> assertThat(myProfile.email()).isEqualTo(String.valueOf(유저1.getEmail()))
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

        when(userRepository.findById(유저1.getId())).thenReturn(Optional.of(유저1));

        userService.updateProfile(updateRequest, 유저1.getId());
        User updatedUser = userRepository.findById(유저1.getId()).get();

        assertAll(
                () -> assertThat(updatedUser.getNickname()).isEqualTo(updateRequest.nickname()),
                () -> assertThat(updatedUser.getBlogTitle()).isEqualTo(updateRequest.blogTitle()),
                () -> assertThat(updatedUser.getProfileImage()).isEqualTo(updateRequest.profileImage())
        );
    }

}
