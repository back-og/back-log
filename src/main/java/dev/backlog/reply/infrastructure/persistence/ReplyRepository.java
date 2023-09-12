package dev.backlog.reply.infrastructure.persistence;

import dev.backlog.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
