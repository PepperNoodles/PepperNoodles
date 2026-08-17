package com.peppernoodles.user.repository;

import com.peppernoodles.user.domain.WallMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WallMessageRepository extends JpaRepository<WallMessage, Long> {

    /** Top-level messages on a wall; replies come with each one. */
    @EntityGraph(attributePaths = {"author", "author.profile"})
    Page<WallMessage> findByWallOwnerIdAndParentIsNullOrderByCreatedAtDesc(Long ownerId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "author.profile"})
    List<WallMessage> findByParentIdInOrderByCreatedAtAsc(List<Long> parentIds);

    @EntityGraph(attributePaths = {"author", "wallOwner"})
    Optional<WallMessage> findDetailedById(Long id);

    @Query("select m.id as messageId, count(u) as likeCount from WallMessage m join m.likedBy u where m.id in :ids group by m.id")
    List<LikeCountRow> countLikes(@Param("ids") List<Long> messageIds);

    @Query("select m.id from WallMessage m join m.likedBy u where m.id in :ids and u.id = :userId")
    List<Long> findLikedIdsBy(@Param("ids") List<Long> messageIds, @Param("userId") Long userId);

    interface LikeCountRow {
        Long getMessageId();

        Long getLikeCount();
    }
}
