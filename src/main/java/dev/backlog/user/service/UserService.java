package dev.backlog.user.service;

import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.UserDetailsResponse;
import dev.backlog.user.dto.UserResponse;
import dev.backlog.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserResponse findUserProfile(String nickname) {
        User user = userRepository.getByNickname(nickname);
        return UserResponse.from(user);
    }

    public UserDetailsResponse findMyProfile(Long userId) {
        User user = userRepository.getById(userId);
        return UserDetailsResponse.from(user);
    }

    @Transactional
    public void updateProfile(UserUpdateRequest request, Long userId) {
        User user = userRepository.getById(userId);
        updateUserByRequest(user, request);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.getById(userId);
        user.markUserAsDeleted();
    }

    private void updateUserByRequest(User user, UserUpdateRequest request) {
        user.updateNickName(request.nickname());
        user.updateEmail(request.email());
        user.updateProfileImage(request.profileImage());
        user.updateIntroduction(request.introduction());
        user.updateBlogTitle(request.blogTitle());
    }

}
