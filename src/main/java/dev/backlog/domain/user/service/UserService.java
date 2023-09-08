package dev.backlog.domain.user.service;

import dev.backlog.domain.user.dto.UserDetailsResponse;
import dev.backlog.domain.user.dto.UserResponse;
import dev.backlog.domain.user.dto.UserUpdateRequest;
import dev.backlog.domain.user.model.User;
import dev.backlog.domain.user.model.repository.UserRepository;
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
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 찾을 수 없습니다."));
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
