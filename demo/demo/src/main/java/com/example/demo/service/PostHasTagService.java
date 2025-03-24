package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.entity.PostHasTag;
import com.example.demo.entity.Tag;
import com.example.demo.repository.PostHasTagRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostHasTagService {

    @Autowired
    private PostHasTagRepository postHasTagRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    public List<PostHasTag> getTagsForPost(Long postId) {
        // Check if post exists
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        List<PostHasTag> tags = postHasTagRepository.findByPostPostId(postId);
        if (tags.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tags found for this post");
        }

        return tags;
    }

    public List<PostHasTag> getPostsForTag(Long tagId) {
        // Check if tag exists
        if (!tagRepository.existsById(tagId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }

        List<PostHasTag> posts = postHasTagRepository.findByTagTagId(tagId);
        if (posts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No posts found for this tag");
        }

        return posts;
    }

    public PostHasTag addTagToPost(Long postId, Long tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));

        // Check if the post already has this tag
        if (postHasTagRepository.existsByPostPostIdAndTagTagId(postId, tagId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This tag is already assigned to the post");
        }

        PostHasTag postHasTag = new PostHasTag(post, tag);
        return postHasTagRepository.save(postHasTag);
    }

    public void removeTagFromPost(Long postId, Long tagId) {
        PostHasTag postHasTag = postHasTagRepository.findByPostPostIdAndTagTagId(postId, tagId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag relation not found"));

        postHasTagRepository.delete(postHasTag);
    }

}
