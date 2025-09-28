package com.blogApplication.service;

import java.util.List;

import com.blogApplication.dto.BlogDto;

public interface BlogService {
	BlogDto createBlog(BlogDto blogDto, String username);
    BlogDto updateBlog(Long id, BlogDto blogDto, String username);
    void deleteBlog(Long id, String username);
    List<BlogDto> getPublicBlogs();
    List<BlogDto> getBlogsByUser(String username);




}
