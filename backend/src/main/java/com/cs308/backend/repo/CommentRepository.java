package com.cs308.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.Comment;

public interface CommentRepository  extends JpaRepository<Comment,Long>{
    

}
