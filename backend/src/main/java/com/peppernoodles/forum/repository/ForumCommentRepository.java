package com.peppernoodles.forum.repository;

import com.peppernoodles.forum.domain.ForumComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumCommentRepository extends JpaRepository<ForumComment, Long> {

    @EntityGraph(attributePaths = {"author", "author.profile", "replies", "replies.author", "replies.author.profile", "replies.replyToUser", "replies.replyToUser.profile"})
    List<ForumComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    @EntityGraph(attributePaths = {"author", "post"})
    Optional<ForumComment> findDetailedById(Long id);
}
