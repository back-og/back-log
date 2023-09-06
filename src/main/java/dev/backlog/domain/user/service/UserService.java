package dev.backlog.domain.user.service;

import dev.backlog.domain.user.dto.UserDetailsResponse;
import dev.backlog.domain.user.dto.UserResponse;
import dev.backlog.domain.user.dto.UserUpdateRequest;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserJpaRepository userJpaRepository;

    public UserResponse findUserProfile(String nickname) {
        User user = userJpaRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 찾을 수 없습니다."));
        return UserResponse.from(user);
    }

    public UserDetailsResponse findMyProfile(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 찾을 수 없습니다."));
        return UserDetailsResponse.from(user);
    }

    @Transactional
    public void updateProfile(UserUpdateRequest request, Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 찾을 수 없습니다."));
        updateUserByRequest(user, request);
    }

    private void updateUserByRequest(User user, UserUpdateRequest request) {
        user.updateNickName(request.nickname());
        user.updateEmail(request.email());
        user.updateProfileImage(request.profileImage());
        user.updateIntroduction(request.introduction());
        user.updateBlogTitle(request.blogTitle());
    }

}
