package com.example.demo.repository;

import com.example.demo.entity.VoteForComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteForCommentRepository extends JpaRepository<VoteForComment, Long> {

    // Custom query to find vote by user and comment
    @Query("SELECT v FROM VoteForComment v WHERE v.user.userId = :userId AND v.comment.commentId = :commentId")
    Optional<VoteForComment> findByUserUserIdAndCommentCommentId(Long userId, Long commentId);

    // Count upvotes for a specific comment
    @Query("SELECT COUNT(v) FROM VoteForComment v WHERE v.comment.commentId = :commentId AND v.voteType = 'UPVOTE'")
    int countUpvotesForComment(@Param("commentId") Long commentId);

    // Count downvotes for a specific comment
    @Query("SELECT COUNT(v) FROM VoteForComment v WHERE v.comment.commentId = :commentId AND v.voteType = 'DOWNVOTE'")
    int countDownvotesForComment(@Param("commentId") Long commentId);
}
