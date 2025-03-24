package com.example.demo.controller;

import com.example.demo.entity.VoteForPost;
import com.example.demo.service.VoteForPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
public class VoteForPostController {

    @Autowired
    private VoteForPostService voteForPostService;

    // Add vote to a post
    @PostMapping("/add/{postId}/{userId}")
    public ResponseEntity<VoteForPost> addVoteToPost(@PathVariable Long postId,
                                                     @PathVariable Long userId,
                                                     @RequestParam VoteForPost.VoteType voteType) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voteForPostService.addVoteToPost(postId, userId, voteType));
    }

    // Get vote count for a post (upvotes - downvotes)
    @GetMapping("/count/{postId}")
    public ResponseEntity<Integer> getVoteCountForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(voteForPostService.getVoteCountForPost(postId));
    }

    // Remove vote from a post
    @DeleteMapping("/remove/{postId}/{userId}")
    public ResponseEntity<String> removeVoteFromPost(@PathVariable Long postId, @PathVariable Long userId) {
        voteForPostService.removeVoteFromPost(postId, userId);
        return ResponseEntity.ok("Vote removed successfully");
    }
}
