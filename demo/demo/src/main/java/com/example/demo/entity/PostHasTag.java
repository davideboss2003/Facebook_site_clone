package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_has_tag", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "tag_id"})
})
public class PostHasTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    public PostHasTag() {
    }

    public PostHasTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
