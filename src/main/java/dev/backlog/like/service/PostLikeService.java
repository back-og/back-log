package dev.backlog.like.service;

import dev.backlog.like.domain.PostLike;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.like.dto.LikeStatusResponse;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public LikeStatusResponse switchLike(Long postId, AuthInfo authInfo) {
        try {
            User user = userRepository.getById(authInfo.userId()); // 사용자 조회
            Post post = postRepository.getById(postId);// 좋아요 누를 게시물
            log.info("현재 좋아요 수 {}", post.getLikeCount());
            Optional<PostLike> postLike = postLikeRepository.findByUserAndPostId(user, postId); // 사용자가 이미 좋아요를 눌렀는지 확인
            log.info("현재 좋아요 유무 {}", postLike.isPresent());
            switchLike(postLike, post, user);

            boolean isLiked = postLike.isEmpty();
            return new LikeStatusResponse(post.getLikeCount(), isLiked);
        } catch (Exception e) {
            log.info("로그 메시지 {}", e);
            return null;
        }
    }

    private void switchLike(Optional<PostLike> postLike, Post post, User user) {
        if (postLike.isPresent()) {  // 좋아요를 눌렀는지 확인
            log.info("감소 회원아이디 {} 포스트아이디 {}", user.getId(), post.getId());
            postLikeRepository.delete(postLike.get()); // 이미 좋아요가 있으면 삭제
            log.info("현재 좋아요 수 {}", post.getLikeCount());
            post.decreaseLikeCount(); // Post의 Like 감소
            return;
        }
        log.info("증가 회원아이디 {} 포스트아이디 {}", user.getId(), post.getId());
        postLikeRepository.save(new PostLike(user, post)); // 좋아요를 누르지 않았으면 좋아요 테이블에 레코드 추가
        post.increaseLikeCount(); // Post의 Like 컬럼 상승
    }

}
