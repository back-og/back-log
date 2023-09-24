package dev.backlog.user.service;

import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.UserDetailsResponse;
import dev.backlog.user.dto.UserResponse;
import dev.backlog.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.backlog.common.fixture.DtoFixture.회원_수정_요청;
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

    private User 유저;

    @BeforeEach
    void setUp() {
        유저 = 유저();
    }

    @Test
    void findUserProfileTest() {
        String nickname = "닉네임";
        when(userRepository.getByNickname(유저.getNickname())).thenReturn(유저);

        UserResponse userProfile = userService.findUserProfile(nickname);

        assertThat(userProfile.nickname()).isEqualTo(nickname);
    }

    @Test
    void findMyProfileTest() {
        Long userId = 유저.getId();

        when(userRepository.getById(유저.getId())).thenReturn(유저);

        UserDetailsResponse myProfile = userService.findMyProfile(userId);

        assertAll(
                () -> assertThat(myProfile.nickname()).isEqualTo(유저.getNickname()),
                () -> assertThat(myProfile.profileImage()).isEqualTo(유저.getProfileImage()),
                () -> assertThat(myProfile.blogTitle()).isEqualTo(유저.getBlogTitle()),
                () -> assertThat(myProfile.introduction()).isEqualTo(유저.getIntroduction()),
                () -> assertThat(myProfile.email()).isEqualTo(유저.getEmail())
        );
    }

    @Test
    void updateUserTest() {
        UserUpdateRequest updateRequest = 회원_수정_요청();

        when(userRepository.getById(유저.getId())).thenReturn(유저);

        userService.updateProfile(updateRequest, 유저.getId());
        User updatedUser = userRepository.getById(유저.getId());

        assertAll(
                () -> assertThat(updatedUser.getNickname()).isEqualTo(updateRequest.nickname()),
                () -> assertThat(updatedUser.getBlogTitle()).isEqualTo(updateRequest.blogTitle()),
                () -> assertThat(updatedUser.getProfileImage()).isEqualTo(updateRequest.profileImage())
        );
    }

}
