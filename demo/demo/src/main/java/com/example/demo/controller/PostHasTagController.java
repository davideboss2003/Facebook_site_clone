package com.example.demo.controller;

import com.example.demo.entity.PostHasTag;
import com.example.demo.service.PostHasTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post-tags")
public class PostHasTagController {

    @Autowired
    private PostHasTagService postHasTagService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<PostHasTag>> getTagsForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postHasTagService.getTagsForPost(postId));
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<PostHasTag>> getPostsForTag(@PathVariable Long tagId) {
        return ResponseEntity.ok(postHasTagService.getPostsForTag(tagId));
    }

    @PostMapping("/add")
    public ResponseEntity<PostHasTag> addTagToPost(@RequestParam Long postId, @RequestParam Long tagId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postHasTagService.addTagToPost(postId, tagId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeTagFromPost(@RequestParam Long postId, @RequestParam Long tagId) {
        postHasTagService.removeTagFromPost(postId, tagId);
        return ResponseEntity.ok("Tag removed from post successfully");
    }
}
