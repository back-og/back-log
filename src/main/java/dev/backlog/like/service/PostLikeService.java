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
@Transactional
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeStatusResponse switchLike(Long postId, AuthInfo authInfo) {
        try {

            User user = userRepository.getById(authInfo.userId());
            Post post = postRepository.getById(postId);
            Optional<PostLike> postLike = postLikeRepository.findByUserAndPostId(user, postId); // 동시성으로 이 곳에서 null이 조회되면 동일한 userid와 postid를 가진 PostLike가 생성됨.

            switchLike(postLike, post, user);

            boolean isLiked = postLike.isEmpty();
            return new LikeStatusResponse(post.getLikeCount(), isLiked);
        } catch (Exception e) {
            log.info("로그 메시지 {}", e);
            return null;
        }
    }

    private void switchLike(Optional<PostLike> postLike, Post post, User user) {
        if (postLike.isPresent()) {
            postLikeRepository.delete(postLike.get());
            post.decreaseLikeCount();
            return;
        }
        postLikeRepository.save(new PostLike(user, post));
        post.increaseLikeCount();
        log.info("포스트 좋아요 {}", post.getLikeCount());
    }

}
