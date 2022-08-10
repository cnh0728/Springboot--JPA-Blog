package com.ramyun.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ramyun.blog.model.User;
import com.ramyun.blog.repository.UserRepository;

//스프링이 컴포턴트 스캔을 통해 Bean에 등록을 해줌 IoC를 해줌.
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public int 회원가입(User user){
        try{
            userRepository.save(user);
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("UserService: 회원가입(): "+e.getMessage());
        }
        return -1;
    }

    @Transactional(readOnly=true) // select할 때 트랜잭션 시작, 서비스 종료시에 트랙잭션 종료 (정합성)
    public User 로그인(User user){
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
