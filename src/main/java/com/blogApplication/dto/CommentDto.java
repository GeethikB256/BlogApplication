package com.blogApplication.dto;

public class CommentDto {
	private Long id;
	private String content;
	private Long userId;
	private Long blogPostId;
	private String userName;
	public CommentDto() {
		
	}
	
	public CommentDto(Long id, String content, Long userId, Long blogPostId, String userName) {
		super();
		this.id = id;
		this.content = content;
		this.userId = userId;
		this.blogPostId = blogPostId;
		this.userName = userName;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getBlogPostId() {
		return blogPostId;
	}
	public void setBlogPostId(Long blogPostId) {
		this.blogPostId = blogPostId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
