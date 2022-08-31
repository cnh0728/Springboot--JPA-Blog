package com.ramyun.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ramyun.blog.controller.dto.ResponseDto;
import com.ramyun.blog.model.RoleType;
import com.ramyun.blog.model.User;
import com.ramyun.blog.service.UserService;

@RestController
public class UserApiController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user){
        System.out.println("UserApiController: save 호출됨.");
        //실제로 DB에 insert를 하고 아래에서 return을 해주면 됨.
        userService.회원가입(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); //자바오브젝트를 jackson이 json으로 변환 
    }
}
