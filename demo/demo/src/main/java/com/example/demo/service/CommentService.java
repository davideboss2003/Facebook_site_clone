package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Comment> getCommentsForPost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        return commentRepository.findByPostPostId(postId);
    }

    public List<Comment> getCommentsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return commentRepository.findByUserUserId(userId);
    }

    public Comment addCommentToPost(Long postId, Long userId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Verificam daca postarea e outdated, astfel incat sa nu se mai poata comenta
        if (post.getStatus() == Post.PostStatus.OUTDATED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Comments are disabled for this post");
        }

        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        // dupa primul comentariu, schimbam statusul postarii din JUST_POSTED in FIRST_REACTIONS
        if (post.getStatus() == Post.PostStatus.JUST_POSTED) {
            post.setStatus(Post.PostStatus.FIRST_REACTIONS);
            postRepository.save(post);
        }

        return savedComment;
    }


    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        commentRepository.deleteById(commentId);
    }
    //nu dam update la comment, pentru ca nu avem la ce, doar la Text dar nu mi se pare car ar fi ok
}
