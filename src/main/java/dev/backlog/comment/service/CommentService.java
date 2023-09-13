package dev.backlog.comment.service;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.comment.dto.CreateCommentRequest;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Long create(CreateCommentRequest request, AuthInfo authInfo, Long postId) {
        User findUser = userRepository.getById(authInfo.userId());
        Post findPost = postRepository.getById(postId);
        Comment comment = request.toEntity(findUser, findPost);

        return commentRepository.save(comment).getId();
    }

}
