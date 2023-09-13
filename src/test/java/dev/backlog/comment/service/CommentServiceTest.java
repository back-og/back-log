package dev.backlog.comment.service;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.comment.dto.CreateCommentRequest;
import dev.backlog.comment.dto.UpdateCommentRequest;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static dev.backlog.common.fixture.EntityFixture.게시물1;
import static dev.backlog.common.fixture.EntityFixture.댓글1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User 유저1;
    private Post 게시물1;
    private Comment 댓글1;

    @BeforeEach
    public void setUp() {
        유저1 = 유저1();
        게시물1 = 게시물1(유저1, null);
        댓글1 = 댓글1(유저1, 게시물1);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("댓글을 생성할 수 있다.")
    @Test
    void createCommentTest() {
        User user = userRepository.save(유저1);
        Post post = postRepository.save(게시물1);

        AuthInfo authInfo = new AuthInfo(user.getId(), "토큰");
        CreateCommentRequest request = new CreateCommentRequest("댓글입니다다다다;");
        Long commentId = commentService.create(request, authInfo, post.getId());

        Comment findComment = commentRepository.getById(commentId);

        assertThat(findComment.getPost().getId()).isEqualTo(게시물1.getId());
        assertThat(findComment.getWriter().getId()).isEqualTo(유저1.getId());
    }

    @DisplayName("댓글 작성자와 로그인한 사용자의 아이디가 같을 경우 댓글을 수정할 수 있다.")
    @Test
    void updateCommentTest() {
        String updateCommentContent = "수정한 댓글립니다다다다다";
        User user = userRepository.save(유저1);
        postRepository.save(게시물1);
        Comment comment = commentRepository.save(댓글1);

        AuthInfo authInfo = new AuthInfo(user.getId(), "토큰");

        UpdateCommentRequest request = new UpdateCommentRequest(updateCommentContent);
        commentService.update(request, authInfo, comment.getId());
        Comment findComment = commentRepository.getById(comment.getId());

        assertThat(updateCommentContent).isEqualTo(findComment.getContent());
    }

}
