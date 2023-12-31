package dev.backlog.comment.api;

import dev.backlog.comment.dto.CommentCreateRequest;
import dev.backlog.comment.dto.CommentUpdateRequest;
import dev.backlog.comment.service.CommentService;
import dev.backlog.common.annotation.Login;
import dev.backlog.user.dto.AuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/v1")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> create(
            @Valid @RequestBody CommentCreateRequest request,
            @Login AuthInfo authInfo,
            @PathVariable Long postId
    ) {
        Long commentId = commentService.create(request, authInfo, postId);
        return ResponseEntity.created(URI.create("/comments/" + commentId)).build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> update(
            @Valid @RequestBody CommentUpdateRequest request,
            @Login AuthInfo authInfo,
            @PathVariable Long commentId
    ) {
        commentService.update(request, authInfo, commentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@Login AuthInfo authInfo, @PathVariable Long commentId) {
        commentService.delete(authInfo, commentId);
        return ResponseEntity.noContent().build();
    }

}
