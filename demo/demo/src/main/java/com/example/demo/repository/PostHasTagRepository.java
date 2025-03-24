package com.example.demo.repository;

import com.example.demo.entity.PostHasTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostHasTagRepository extends JpaRepository<PostHasTag, Long> {
    List<PostHasTag> findByPostPostId(Long postId);

    List<PostHasTag> findByTagTagId(Long tagId);

    // Check if a tag is already assigned to a post
    boolean existsByPostPostIdAndTagTagId(Long postId, Long tagId);

    Optional<PostHasTag> findByPostPostIdAndTagTagId(Long postId, Long tagId);

}
