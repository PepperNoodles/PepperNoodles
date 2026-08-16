package com.peppernoodles.user.repository;

import com.peppernoodles.user.domain.Friendship;
import com.peppernoodles.user.domain.Friendship.Status;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    /** Finds the relationship between two accounts regardless of who asked first. */
    @Query("""
            select f from Friendship f
             where (f.requester.id = :a and f.addressee.id = :b)
                or (f.requester.id = :b and f.addressee.id = :a)
            """)
    Optional<Friendship> findBetween(@Param("a") Long userA, @Param("b") Long userB);

    @EntityGraph(attributePaths = {"requester", "requester.profile", "addressee", "addressee.profile"})
    @Query("""
            select f from Friendship f
             where f.status = :status
               and (f.requester.id = :userId or f.addressee.id = :userId)
             order by f.respondedAt desc nulls last, f.createdAt desc
            """)
    List<Friendship> findAllForUser(@Param("userId") Long userId, @Param("status") Status status);

    /** Requests awaiting this user's answer. */
    @EntityGraph(attributePaths = {"requester", "requester.profile"})
    List<Friendship> findByAddresseeIdAndStatus(Long addresseeId, Status status);

    /** Requests this user has sent and not yet had answered. */
    @EntityGraph(attributePaths = {"addressee", "addressee.profile"})
    List<Friendship> findByRequesterIdAndStatus(Long requesterId, Status status);
}
