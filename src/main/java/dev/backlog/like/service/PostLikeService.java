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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public LikeStatusResponse toggleLikeStatus(Long postId, AuthInfo authInfo) {
        User user = userRepository.getById(authInfo.userId());
        Post post = postRepository.getById(postId);

        PostLike postLike = postLikeRepository.findByUserAndPostId(user, postId).orElse(null);

        updateLikeStatus(postLike, post, user);

        boolean isLiked = postLike == null;
        return new LikeStatusResponse(post.getLikeCount(), isLiked);
    }

    private void updateLikeStatus(PostLike postLike, Post post, User user) {
        if (postLike != null) {
            removeLike(postLike, post);
        } else {
            addLike(post, user);
        }
    }

    private void removeLike(PostLike postLike, Post post) {
        postLikeRepository.delete(postLike);
        post.decreaseLikeCount();
    }

    private void addLike(Post post, User user) {
        postLikeRepository.save(new PostLike(user, post));
        post.increaseLikeCount();
    }

}
