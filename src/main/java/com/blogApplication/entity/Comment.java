package com.blogApplication.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "comments")
public class Comment {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank(message = "Comment content is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_post_id", nullable = false)
    private BlogPost blogPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
 
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
 
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { 
    	return id; 
    	}
    
    public void setId(Long id) { 
    	this.id = id; 
    	}
 
    public String getContent() { 
    	return content; 
    	}
    public void setContent(String content) {
    	this.content = content;
    	}
 
    public LocalDateTime getCreatedAt() { 
    	return createdAt;
    	}
    public void setCreatedAt(LocalDateTime createdAt) {
    	this.createdAt = createdAt; 
    	}
 
    public LocalDateTime getUpdatedAt() { 
    	return updatedAt; 
    	}
    public void setUpdatedAt(LocalDateTime updatedAt) {
    	this.updatedAt = updatedAt; 
    	}
 
    public BlogPost getBlogPost() { 
    	return blogPost;
    	}
    public void setBlogPost(BlogPost blogPost) { 
    	this.blogPost = blogPost;
    	}
 
    public User getUser() {
    	return user; 
    	}
    public void setUser(User user) { 
    	this.user = user;
    	}

	
}
 