package com.example.demo.controller;

import com.example.demo.entity.VoteForComment;
import com.example.demo.service.VoteForCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment-votes")
public class VoteForCommentController {

    @Autowired
    private VoteForCommentService voteForCommentService;

    // Add vote to a comment
    @PostMapping("/add/{commentId}/{userId}")
    public ResponseEntity<VoteForComment> addVoteToComment(@PathVariable Long commentId,
                                                           @PathVariable Long userId,
                                                           @RequestParam VoteForComment.VoteType voteType) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voteForCommentService.addVoteToComment(commentId, userId, voteType));
    }

    // Get vote count for a comment (upvotes - downvotes)
    @GetMapping("/count/{commentId}")
    public ResponseEntity<Integer> getVoteCountForComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(voteForCommentService.getVoteCountForComment(commentId));
    }

    // Remove vote from a comment
    @DeleteMapping("/remove/{commentId}/{userId}")
    public ResponseEntity<String> removeVoteFromComment(@PathVariable Long commentId, @PathVariable Long userId) {
        voteForCommentService.removeVoteFromComment(commentId, userId);
        return ResponseEntity.ok("Vote removed successfully");
    }
}
