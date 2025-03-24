package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.entity.VoteForComment;
import com.example.demo.repository.VoteForCommentRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class VoteForCommentService {

    @Autowired
    private VoteForCommentRepository voteForCommentRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    // Add vote to a comment
    public VoteForComment addVoteToComment(Long commentId, Long userId, VoteForComment.VoteType voteType) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (comment.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot vote on their own comment");
        }

        Optional<VoteForComment> existingVote = voteForCommentRepository.findByUserUserIdAndCommentCommentId(userId, commentId);
        if (existingVote.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already voted on this comment");
        }

        VoteForComment voteForComment = new VoteForComment();
        voteForComment.setUser(userRepository.findById(userId).get());
        voteForComment.setComment(comment);
        voteForComment.setVoteType(voteType);

        // Update the author's score based on vote type
        User commentAuthor = comment.getUser();
        if (voteType == VoteForComment.VoteType.UPVOTE) {
            commentAuthor.setScore((float) (commentAuthor.getScore() + 5));
        } else {
            commentAuthor.setScore((float) (commentAuthor.getScore() - 2.5));
        }

        if (voteType == VoteForComment.VoteType.DOWNVOTE) {
            User votingUser = userRepository.findById(userId).get();
            votingUser.setScore((float) (votingUser.getScore() - 1.5));
            userRepository.save(votingUser);
        }

        userRepository.save(commentAuthor); // Persist score update
        return voteForCommentRepository.save(voteForComment);
    }


    // Get vote count for a comment (upvotes - downvotes)
    public int getVoteCountForComment(Long commentId) {
        // Check if the comment exists
        if (!commentRepository.existsById(commentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }

        // Count the upvotes and downvotes separately
        int upvoteCount = voteForCommentRepository.countUpvotesForComment(commentId);
        int downvoteCount = voteForCommentRepository.countDownvotesForComment(commentId);

        // Return the difference (upvotes - downvotes)
        return upvoteCount - downvoteCount;
    }

    // Remove vote for a comment
    public void removeVoteFromComment(Long commentId, Long userId) {
        VoteForComment voteForComment = voteForCommentRepository.findByUserUserIdAndCommentCommentId(userId, commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found"));

        voteForCommentRepository.delete(voteForComment);
    }
}
