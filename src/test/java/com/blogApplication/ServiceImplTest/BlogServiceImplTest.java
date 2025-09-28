package com.blogApplication.ServiceImplTest;

import com.blogApplication.dto.BlogDto;
import com.blogApplication.entity.BlogPost;
import com.blogApplication.entity.User;
import com.blogApplication.exception.ResourceNotFoundException;
import com.blogApplication.repository.BlogRepository;
import com.blogApplication.repository.UserRepository;
import com.blogApplication.serviceImpl.BlogServiceImpl;
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

    @Test
    void testCreateBlog_Success() {
        BlogDto blogDto = new BlogDto(null, "Title", "Content", true, null);
        User user = new User();
        user.setUsername("john");

        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Title");
        blogPost.setContent("Content");
        blogPost.setPublic(true);
        blogPost.setUser(user);

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(blogRepository.save(any(BlogPost.class))).thenReturn(blogPost);

        BlogDto result = blogService.createBlog(blogDto, "john");

        assertEquals("Title", result.getTitle());
        assertEquals("john", result.getUserName());
    }
    @Test
    void testCreateBlog_UserNotFound() {
        BlogDto dto = new BlogDto(null, "Test Title", "Test Content", true, "unknown");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> blogService.createBlog(dto, "unknown"));
    }

    @Test
    void testUpdateBlog_Success() {
        User user = new User();
        user.setUsername("john");

        BlogPost existingBlog = new BlogPost();
        existingBlog.setId(1L);
        existingBlog.setUser(user);

        BlogDto updateDto = new BlogDto(null, "Updated Title", "Updated Content", false, null);

        when(blogRepository.findById(1L)).thenReturn(Optional.of(existingBlog));
        when(blogRepository.save(any(BlogPost.class))).thenReturn(existingBlog);

        BlogDto result = blogService.updateBlog(1L, updateDto, "john");

        assertEquals("Updated Title", result.getTitle());
        assertFalse(result.isPublic());
    }

    @Test
    void testUpdateBlog_UnauthorizedUser() {
        User user = new User();
        user.setUsername("john");

        BlogPost blog = new BlogPost();
        blog.setId(1L);
        blog.setUser(user);

        BlogDto updateDto = new BlogDto(null, "Updated", "Updated", true, null);

        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));

        assertThrows(ResourceNotFoundException.class, () -> {
            blogService.updateBlog(1L, updateDto, "jane");
        });
    }

    @Test
    void testDeleteBlog_Success() {
        User user = new User();
        user.setUsername("john");

        BlogPost blog = new BlogPost();
        blog.setId(1L);
        blog.setUser(user);

        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));

        blogService.deleteBlog(1L, "john");

        verify(blogRepository).delete(blog);
    }

    @Test
    void testDeleteBlog_UnauthorizedUser() {
        User user = new User();
        user.setUsername("john");

        BlogPost blog = new BlogPost();
        blog.setId(1L);
        blog.setUser(user);

        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));

        assertThrows(ResourceNotFoundException.class, () -> {
            blogService.deleteBlog(1L, "jane");
        });
    }

    @Test
    void testGetPublicBlogs() {
        User user = new User();
        user.setUsername("john");

        BlogPost blog = new BlogPost();
        blog.setId(1L);
        blog.setTitle("Public Blog");
        blog.setContent("Content");
        blog.setPublic(true);
        blog.setUser(user);

        when(blogRepository.findByIsPublicTrue()).thenReturn(List.of(blog));

        List<BlogDto> result = blogService.getPublicBlogs();

        assertEquals(1, result.size());
        assertEquals("Public Blog", result.get(0).getTitle());
    }

    @Test
    void testGetBlogsByUser() {
        User user = new User();
        user.setUsername("john");

        BlogPost blog = new BlogPost();
        blog.setId(1L);
        blog.setTitle("User Blog");
        blog.setContent("Content");
        blog.setPublic(false);
        blog.setUser(user);

        when(blogRepository.findByUserUsername("john")).thenReturn(List.of(blog));

        List<BlogDto> result = blogService.getBlogsByUser("john");

        assertEquals(1, result.size());
        assertEquals("User Blog", result.get(0).getTitle());
    }
}
