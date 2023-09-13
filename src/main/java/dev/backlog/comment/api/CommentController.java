package dev.backlog.comment.api;

import dev.backlog.comment.dto.CreateCommentRequest;
import dev.backlog.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> create(CreateCommentRequest request, Long userId, Long postId) {
        commentService.create(request, userId, postId);

        return ResponseEntity.ok().build();
    }

}
