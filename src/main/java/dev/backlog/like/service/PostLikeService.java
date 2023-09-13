package dev.backlog.like.service;

import dev.backlog.like.domain.PostLike;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.like.dto.LikeStatus;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeStatus switchLike(Long postId, AuthInfo authInfo) {
        User user = userRepository.getById(authInfo.userId());
        Post post = postRepository.getById(postId);
        Optional<PostLike> postLike = postLikeRepository.findByUserAndPostId(user, postId);

        switchLike(postLike, post, user);

        int likeCount = postLikeRepository.countByPost(post);
        boolean isLiked = postLike.isEmpty();
        return new LikeStatus(likeCount, isLiked);
    }

    private void switchLike(Optional<PostLike> postLike, Post post, User user) {
        if (postLike.isPresent()) {
            postLikeRepository.delete(postLike.get());
            return;
        }
        postLikeRepository.save(new PostLike(user, post));
    }

}
