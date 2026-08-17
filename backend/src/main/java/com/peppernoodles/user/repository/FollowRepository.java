package com.peppernoodles.user.repository;

import com.peppernoodles.user.domain.Follow;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Follow.Key> {

    @EntityGraph(attributePaths = {"followee", "followee.profile"})
    List<Follow> findByFollowerId(Long followerId);

    @EntityGraph(attributePaths = {"follower", "follower.profile"})
    List<Follow> findByFolloweeId(Long followeeId);

    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    long countByFolloweeId(Long followeeId);

    long countByFollowerId(Long followerId);

    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
