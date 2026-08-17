package com.peppernoodles.forum.repository;

import com.peppernoodles.forum.domain.ForumPost;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {

    // Only one collection is fetched here; pulling tags and bookmarks together
    // would multiply the rows.
    @EntityGraph(attributePaths = {"author", "author.profile", "tags"})
    Page<ForumPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "author.profile", "tags"})
    Optional<ForumPost> findDetailedById(Long id);

    @EntityGraph(attributePaths = {"author", "author.profile", "tags"})
    Page<ForumPost> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "author.profile", "tags"})
    @Query("""
            select p from ForumPost p
              join p.tags t
             where t.id in :tagIds
             order by p.createdAt desc
            """)
    Page<ForumPost> findByTagIds(@Param("tagIds") java.util.List<Long> tagIds, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "author.profile", "tags"})
    @Query("""
            select p from ForumPost p
              join p.bookmarkedBy u
             where u.id = :userId
             order by p.createdAt desc
            """)
    Page<ForumPost> findBookmarkedBy(@Param("userId") Long userId, Pageable pageable);

    @Query("select count(u) from ForumPost p join p.bookmarkedBy u where p.id = :postId")
    long countBookmarks(@Param("postId") Long postId);

    @Query("select count(u) > 0 from ForumPost p join p.bookmarkedBy u where p.id = :postId and u.id = :userId")
    boolean isBookmarkedBy(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query("select count(c) from ForumComment c where c.post.id = :postId")
    long countComments(@Param("postId") Long postId);
}
