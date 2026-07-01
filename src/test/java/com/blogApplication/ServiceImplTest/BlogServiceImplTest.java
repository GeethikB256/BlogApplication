package com.blogApplication.ServiceImplTest;

import com.blogApplication.dto.BlogDto;
import com.blogApplication.entity.BlogPost;
import com.blogApplication.entity.User;
import com.blogApplication.exception.ResourceNotFoundException;
import com.blogApplication.repository.BlogRepository;
import com.blogApplication.repository.UserRepository;
import com.blogApplication.serviceImpl.BlogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlogServiceImplTest {

    @InjectMocks
    private BlogServiceImpl blogService;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private BlogPost blogPost;
    private BlogDto blogDto;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("john");

        blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Title");
        blogPost.setContent("Content");
        blogPost.setPublic(true);
        blogPost.setUser(user);

        blogDto = new BlogDto(null, "Title", "Content", true, null);
    }

    @Test
    void testCreateBlog_Success() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(blogRepository.save(any(BlogPost.class))).thenReturn(blogPost);

        BlogDto result = blogService.createBlog(blogDto, "john");

        assertEquals("Title", result.getTitle());
        assertEquals("john", result.getUserName());
    }

    @Test
    void testCreateBlog_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        BlogDto dto = new BlogDto(null, "Test Title", "Test Content", true, "unknown");

        assertThrows(ResourceNotFoundException.class, () -> blogService.createBlog(dto, "unknown"));
    }

    @Test
    void testUpdateBlog_Success() {
        BlogDto updateDto = new BlogDto(null, "Updated Title", "Updated Content", false, null);

        when(blogRepository.findById(1L)).thenReturn(Optional.of(blogPost));
        when(blogRepository.save(any(BlogPost.class))).thenReturn(blogPost);

        BlogDto result = blogService.updateBlog(1L, updateDto, "john");

        assertEquals("Updated Title", result.getTitle());
        assertFalse(result.isPublic());
    }

    @Test
    void testUpdateBlog_UnauthorizedUser() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blogPost));

        BlogDto updateDto = new BlogDto(null, "Updated", "Updated", true, null);

        assertThrows(ResourceNotFoundException.class, () -> blogService.updateBlog(1L, updateDto, "jane"));
    }

    @Test
    void testDeleteBlog_Success() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blogPost));

        blogService.deleteBlog(1L, "john");

        verify(blogRepository).delete(blogPost);
    }

    @Test
    void testDeleteBlog_UnauthorizedUser() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blogPost));

        assertThrows(ResourceNotFoundException.class, () -> blogService.deleteBlog(1L, "jane"));
    }

    @Test
    void testGetPublicBlogs() {
        when(blogRepository.findByIsPublicTrue()).thenReturn(List.of(blogPost));

        List<BlogDto> result = blogService.getPublicBlogs();

        assertEquals(1, result.size());
        assertEquals("Title", result.get(0).getTitle());
    }

    @Test
    void testGetBlogsByUser() {
        when(blogRepository.findByUserUsername("john")).thenReturn(List.of(blogPost));

        List<BlogDto> result = blogService.getBlogsByUser("john");

        assertEquals(1, result.size());
        assertEquals("Title", result.get(0).getTitle());
    }
}