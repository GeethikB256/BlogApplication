package com.blogApplication.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogApplication.entity.BlogPost;

@Repository
public interface BlogRepository extends JpaRepository<BlogPost, Long>{

	List<BlogPost> findByIsPublicTrue();

    List<BlogPost> findByUserId(Long userId);

    List<BlogPost> findByUserUsername(String username);

}
