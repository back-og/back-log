package dev.backlog.comment.api;

import java.net.URI;
import dev.backlog.comment.dto.CreateCommentRequest;
import dev.backlog.comment.service.CommentService;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> create(
            @RequestBody CreateCommentRequest request,
            AuthInfo authInfo,
            @PathVariable Long postId
    ) {
        Long commentId = commentService.create(request, authInfo, postId);
        return ResponseEntity.created(URI.create("/comments/" + commentId)).build();
    }

}
