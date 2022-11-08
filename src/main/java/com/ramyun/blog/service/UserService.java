package com.ramyun.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ramyun.blog.model.RoleType;
import com.ramyun.blog.model.User;
import com.ramyun.blog.repository.UserRepository;

//스프링이 컴포턴트 스캔을 통해 Bean에 등록을 해줌 IoC를 해줌.
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional(readOnly = true)
    public User 회원찾기(String username){
        User user = userRepository.findByUsername(username).orElseGet(()->{
            return new User();
        });
        return user;
    }


    @Transactional
    public void 회원가입(User user){
        String rawPassword=user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);
        userRepository.save(user);
    }
    @Transactional
    public void 회원수정(User user){
        //수정시에는 영속성컨텍스트에 있는 User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정
        //select를 해서 User오븢게트를 DB로 부터 가져오는 이유는 영속화를 하기 위해서
        //영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날림
        User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
            return new IllegalArgumentException("회원 찾기 실패");
        });

        //Validate 체크
        if(persistance.getOauth() == null || persistance.getOauth().equals("")){
            String rawPassword = user.getPassword();
            String encPassword = encoder.encode(rawPassword);
            persistance.setPassword(encPassword);
            persistance.setEmail(user.getEmail());
        }

        //회원 수정 함수종료시 = 서비스 종료 = 트랜잭션 종료 = 커밋이 자동으로 됨
        //영속화된 persistance 객체의 변화가 감지되면 더티체킹 (Transactional)이 되어 update문을 날림.
    }
}
