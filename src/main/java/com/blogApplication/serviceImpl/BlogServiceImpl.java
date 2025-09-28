package com.blogApplication.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogApplication.dto.BlogDto;
import com.blogApplication.entity.BlogPost;
import com.blogApplication.entity.User;
import com.blogApplication.exception.ResourceNotFoundException;
import com.blogApplication.repository.BlogRepository;
import com.blogApplication.repository.UserRepository;
import com.blogApplication.service.BlogService;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private UserRepository userRepository;

	private BlogDto mapToDto(BlogPost blog) {
		BlogDto dto = new BlogDto();
		dto.setId(blog.getId());
		dto.setTitle(blog.getTitle());
		dto.setContent(blog.getContent());
		dto.setPublic(blog.isPublic());
		dto.setUserName(blog.getUser().getUsername());
		return dto;
	}

	private BlogPost mapToEntity(BlogDto dto, User user) {
		BlogPost blog = new BlogPost();
		blog.setTitle(dto.getTitle());
		blog.setContent(dto.getContent());
		blog.setPublic(dto.isPublic());
		blog.setUser(user);
		return blog;
	}

	public BlogDto createBlog(BlogDto blogDto, String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		BlogPost blog = mapToEntity(blogDto, user);
		BlogPost savedBlog = blogRepository.save(blog);
		return mapToDto(savedBlog);
	}

	public BlogDto updateBlog(Long id, BlogDto blogDto, String username) {
		BlogPost blog = blogRepository.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

		if (!blog.getUser().getUsername().equals(username)) {
			throw new ResourceNotFoundException("You can only update your own blogs");
		}

		blog.setTitle(blogDto.getTitle());
		blog.setContent(blogDto.getContent());
		blog.setPublic(blogDto.isPublic());
		BlogPost updatedBlog = blogRepository.save(blog);
		return mapToDto(updatedBlog);
	}

	public void deleteBlog(Long id, String username) {
		BlogPost blog = blogRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

		if (!blog.getUser().getUsername().equals(username)) {
			throw new ResourceNotFoundException("You can only delete your own blogs");
		}

		blogRepository.delete(blog);
	}

	public List<BlogDto> getPublicBlogs() {
		List<BlogPost> blogs = blogRepository.findByIsPublicTrue();
		return blogs.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	public List<BlogDto> getBlogsByUser(String username) {
		List<BlogPost> blogs = blogRepository.findByUserUsername(username);
		return blogs.stream().map(this::mapToDto).collect(Collectors.toList());
	}
}
