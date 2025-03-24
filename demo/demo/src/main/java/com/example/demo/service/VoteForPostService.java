package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.entity.VoteForPost;
import com.example.demo.repository.VoteForPostRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class VoteForPostService {

    @Autowired
    private VoteForPostRepository voteForPostRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // Add vote to a post
    public VoteForPost addVoteToPost(Long postId, Long userId, VoteForPost.VoteType voteType) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (post.getAuthor().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot vote on their own post");
        }

        Optional<VoteForPost> existingVote = voteForPostRepository.findByUserUserIdAndPostPostId(userId, postId);
        if (existingVote.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already voted on this post");
        }

        VoteForPost voteForPost = new VoteForPost();
        voteForPost.setUser(userRepository.findById(userId).get());
        voteForPost.setPost(post);
        voteForPost.setVoteType(voteType);

        // Update the author's score based on vote type
        User postAuthor = post.getAuthor();
        if (voteType == VoteForPost.VoteType.UPVOTE) {
            postAuthor.setScore((float) (postAuthor.getScore() + 2.5));
        } else {
            postAuthor.setScore((float) (postAuthor.getScore() - 1.5));
        }

        userRepository.save(postAuthor); // Persist score update
        return voteForPostRepository.save(voteForPost);
    }


    // Get vote count for a post (upvotes - downvotes)
    public int getVoteCountForPost(Long postId) {
        // Check if the post exists
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        // Count the upvotes and downvotes separately
        int upvoteCount = voteForPostRepository.countUpvotesForPost(postId);
        int downvoteCount = voteForPostRepository.countDownvotesForPost(postId);

        // Return the difference (upvotes - downvotes)
        return upvoteCount - downvoteCount;
    }


    // Remove vote for a post
    public void removeVoteFromPost(Long postId, Long userId) {
        VoteForPost voteForPost = voteForPostRepository.findByUserUserIdAndPostPostId(userId, postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found"));

        voteForPostRepository.delete(voteForPost);
    }
}
