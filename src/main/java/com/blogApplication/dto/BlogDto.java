package com.blogApplication.dto;

public class BlogDto {
	private Long id;
	private String title;
	private String content;
	private boolean isPublic;
	private String userName;
	public BlogDto() {
		
	}
	public BlogDto(Long id, String title, String content, 
			boolean isPublic, String userName) {
	
		this.id = id;
		this.title = title;
		this.content = content;
		this.isPublic = isPublic;
		this.userName = userName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}