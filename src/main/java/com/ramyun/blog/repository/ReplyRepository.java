package com.ramyun.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramyun.blog.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    
}
