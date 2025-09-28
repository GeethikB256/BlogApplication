package com.blogApplication.service;

import java.util.List;

import com.blogApplication.dto.CommentDto;
import org.springframework.security.core.Authentication;

public interface CommentService {
	CommentDto updateComment(Long commentId, CommentDto commentDto );
	void deleteComment(Long commentId);
	List<CommentDto> getCommentsByBlogPost(Long blogPostId, Authentication authentication);
	void deleteCommentByBlogcreator(Long blogId, Long commentId);
	CommentDto addComment(Long blogPostId, CommentDto commentDto);
}
