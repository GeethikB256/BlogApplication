package com.blogApplication.controller;

import com.blogApplication.dto.CommentDto;
import com.blogApplication.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
 
    @Autowired
    private CommentService commentService;

    @PostMapping("/add/{blogId}")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long blogId,
            @RequestBody CommentDto commentDto
    ) {
        CommentDto saved = commentService.addComment(blogId, commentDto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentDto commentDto
    ) {
        CommentDto updated = commentService.updateComment(commentId, commentDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comment deleted successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteByCreator/{blogId}/{commentId}")
    public ResponseEntity<Map<String, String>> deleteCommentByBlogCreator(
            @PathVariable Long blogId,
            @PathVariable Long commentId
    ) {
        commentService.deleteCommentByBlogcreator(blogId, commentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comment deleted by blog creator successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/byBlog/{blogId}")
    public ResponseEntity<List<CommentDto>> getCommentsByBlog(
            @PathVariable Long blogId,
            Authentication authentication) {
        List<CommentDto> comments = commentService.getCommentsByBlogPost(blogId, authentication);
        return ResponseEntity.ok(comments);
    }
}
 