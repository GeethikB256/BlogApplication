package com.blogApplication.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blogApplication.dto.CommentDto;
import com.blogApplication.entity.BlogPost;
import com.blogApplication.entity.Comment;
import com.blogApplication.entity.User;
import com.blogApplication.exception.ResourceNotFoundException;
import com.blogApplication.repository.BlogRepository;
import com.blogApplication.repository.CommentRepository;
import com.blogApplication.repository.UserRepository;
import com.blogApplication.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
 
    @Autowired
    private CommentRepository commentRepository;
 
    @Autowired
    private BlogRepository blogRepository;
 
    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private CommentDto mapToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getBlogPost().getId(),
                comment.getUser().getUsername()
        );
    }

    private Comment mapToEntity(CommentDto dto, BlogPost blogPost, User user) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setBlogPost(blogPost);
        comment.setUser(user);
        return comment;
    }

    public CommentDto addComment(Long blogPostId,CommentDto commentDto) {
        User user = getCurrentUser();
        BlogPost blogPost = blogRepository.findById(blogPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found"));
        Comment comment = mapToEntity(commentDto, blogPost, user);
        Comment saved = commentRepository.save(comment);
        return mapToDto(saved);
    }
 

    public CommentDto updateComment(Long commentId, CommentDto commentDto) {
        User user = getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
 
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only edit your own comment");
        }
 
        comment.setContent(commentDto.getContent());
        Comment updated = commentRepository.save(comment);
        return mapToDto(updated);
    }
 

    public void deleteComment(Long commentId) {
        User user = getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
 
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own comment");
        }
 
        commentRepository.delete(comment);
    }

    public void deleteCommentByBlogcreator(Long blogId, Long commentId) {
        User user = getCurrentUser();
        BlogPost blogPost = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        if (!blogPost.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Only the blog creator can delete comments on their blog");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        commentRepository.delete(comment);
    }

    public List<CommentDto> getCommentsByBlogPost(Long blogPostId, Authentication authentication) {
        BlogPost blog = blogRepository.findById(blogPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with ID: " + blogPostId));

        if (!blog.isPublic()) {
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new ResourceNotFoundException("Authentication required to view comments on private blogs");
            }
        }
        return commentRepository.findByBlogPostId(blogPostId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}