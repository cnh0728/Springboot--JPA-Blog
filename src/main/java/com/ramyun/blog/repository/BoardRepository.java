package com.ramyun.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ramyun.blog.model.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{

}
