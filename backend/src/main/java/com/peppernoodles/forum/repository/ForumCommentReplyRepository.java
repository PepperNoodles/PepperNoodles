package com.peppernoodles.forum.repository;

import com.peppernoodles.forum.domain.ForumCommentReply;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumCommentReplyRepository extends JpaRepository<ForumCommentReply, Long> {

    @EntityGraph(attributePaths = {"author", "comment", "comment.post"})
    Optional<ForumCommentReply> findDetailedById(Long id);
}
