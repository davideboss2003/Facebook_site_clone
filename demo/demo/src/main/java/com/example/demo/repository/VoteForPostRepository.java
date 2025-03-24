package com.example.demo.repository;

import com.example.demo.entity.VoteForPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteForPostRepository extends JpaRepository<VoteForPost, Long> {

    // Custom query to find vote by user and post
    @Query("SELECT v FROM VoteForPost v WHERE v.user.userId = :userId AND v.post.postId = :postId")
    Optional<VoteForPost> findByUserUserIdAndPostPostId(Long userId, Long postId);

    // Get votes for a specific post
    List<VoteForPost> findByPostPostId(Long postId);

    // Count upvotes for a specific post
    @Query("SELECT COUNT(v) FROM VoteForPost v WHERE v.post.postId = :postId AND v.voteType = 'UPVOTE'")
    int countUpvotesForPost(@Param("postId") Long postId);

    // Count downvotes for a specific post
    @Query("SELECT COUNT(v) FROM VoteForPost v WHERE v.post.postId = :postId AND v.voteType = 'DOWNVOTE'")
    int countDownvotesForPost(@Param("postId") Long postId);

}
