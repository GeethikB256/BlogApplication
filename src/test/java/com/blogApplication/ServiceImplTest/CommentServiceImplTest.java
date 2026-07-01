package com.blogApplication.ServiceImplTest;

import com.blogApplication.dto.CommentDto;
import com.blogApplication.entity.BlogPost;
import com.blogApplication.entity.Comment;
import com.blogApplication.entity.User;
import com.blogApplication.exception.ResourceNotFoundException;
import com.blogApplication.repository.BlogRepository;
import com.blogApplication.repository.CommentRepository;
import com.blogApplication.repository.UserRepository;
import com.blogApplication.serviceImpl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    private User user;
    private BlogPost blogPost;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        user = new User();
        user.setId(1L);
        user.setUsername("john");

        blogPost = new BlogPost();
        blogPost.setId(100L);
        blogPost.setUser(user);
        blogPost.setPublic(true);

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Nice post!");
        comment.setUser(user);
        comment.setBlogPost(blogPost);

        commentDto = new CommentDto(null, "Nice post!", null, 100L, null);
    }

    @Test
    void testAddComment_Success() {
        when(authentication.getName()).thenReturn("john");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(blogRepository.findById(100L)).thenReturn(Optional.of(blogPost));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = commentService.addComment(100L, commentDto);

        assertEquals("Nice post!", result.getContent());
        assertEquals(1L, result.getId());
        assertEquals("john", result.getUserName());
    }

    @Test
    void testUpdateComment_Success() {
        CommentDto updateDto = new CommentDto(null, "Updated comment", null, null, null);
        comment.setContent("Updated comment");

        when(authentication.getName()).thenReturn("john");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = commentService.updateComment(1L, updateDto);

        assertEquals("Updated comment", result.getContent());
    }

    @Test
    void testUpdateComment_UnauthorizedUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        comment.setUser(otherUser);

        CommentDto updateDto = new CommentDto(null, "Updated", null, null, null);

        when(authentication.getName()).thenReturn("john");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        assertThrows(RuntimeException.class, () -> commentService.updateComment(1L, updateDto));
    }

    @Test
    void testDeleteComment_Success() {
        when(authentication.getName()).thenReturn("john");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(commentRepository).delete(comment);
    }

    @Test
    void testDeleteCommentByBlogCreator_Success() {
        when(authentication.getName()).thenReturn("john");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(blogRepository.findById(100L)).thenReturn(Optional.of(blogPost));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteCommentByBlogcreator(100L, 1L);

        verify(commentRepository).delete(comment);
    }

    @Test
    void testGetCommentsByBlogPost_PublicBlog() {
        when(blogRepository.findById(100L)).thenReturn(Optional.of(blogPost));
        when(commentRepository.findByBlogPostId(100L)).thenReturn(List.of(comment));

        List<CommentDto> result = commentService.getCommentsByBlogPost(100L, null);

        assertEquals(1, result.size());
        assertEquals("Nice post!", result.get(0).getContent());
    }

    @Test
    void testGetCommentsByBlogPost_PrivateBlog_Unauthenticated() {
        blogPost.setPublic(false);

        when(blogRepository.findById(100L)).thenReturn(Optional.of(blogPost));
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.getCommentsByBlogPost(100L, authentication);
        });
    }
}