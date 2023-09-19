package dev.backlog.comment.domain;

import java.util.ArrayList;
import java.util.List;
import dev.backlog.common.entity.BaseEntity;
import dev.backlog.post.domain.Post;
import dev.backlog.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Column(nullable = false, length = 50)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    private Comment(
            User writer,
            Post post,
            String content,
            boolean isDeleted
    ) {
        this.writer = writer;
        this.post = post;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateParent(Comment parent){
        this.parent = parent;
        parent.getChildren().add(this);
    }

}
