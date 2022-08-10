package com.ramyun.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ramyun.blog.model.User;

// JPARepository = DAO
// 자동으로 bean등록이 된다.
// @Repository //생략 가능
public interface UserRepository extends JpaRepository<User, Integer>{
    //JPA Naming 쿼리
    //SELECT * FROM user WEHRE username = ? AND password = ?;
    User findByUsernameAndPassword(String username,String password);
    //위랑 이거랑 똑같다.
    // @Query(value = "SELECT * FROM user WEHRE username = ?1 AND password = ?2;", nativeQuery = true)
    // User login(String username, String password); 
}