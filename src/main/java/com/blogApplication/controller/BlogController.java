package com.blogApplication.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogApplication.dto.BlogDto;
import com.blogApplication.service.BlogService;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
 
    @Autowired
    private BlogService blogService;

    @PostMapping("/create")
    public ResponseEntity<BlogDto> createBlog(@RequestBody BlogDto blogDto,
                                              Authentication authentication) {
        String username = authentication.getName(); // from JWT
        BlogDto createdBlog = blogService.createBlog(blogDto, username);
        return ResponseEntity.ok(createdBlog);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateBlog(@PathVariable Long id,
                                                          @RequestBody BlogDto blogDto,
                                                          Authentication authentication) {
        String username = authentication.getName();
        BlogDto updatedBlog = blogService.updateBlog(id, blogDto, username);
 
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Blog updated successfully");
        response.put("blog", updatedBlog);
 
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteBlog(@PathVariable Long id,
                                                          Authentication authentication) {
        String username = authentication.getName();
        blogService.deleteBlog(id, username);
 
        Map<String, String> response = new HashMap<>();
        response.put("message", "Blog deleted successfully");
 
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public")
    public ResponseEntity<List<BlogDto>> getPublicBlogs() {
        List<BlogDto> blogs = blogService.getPublicBlogs();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BlogDto>> getMyBlogs(Authentication authentication) {
        String username = authentication.getName();
        List<BlogDto> blogs = blogService.getBlogsByUser(username);
        return ResponseEntity.ok(blogs);
    }
}