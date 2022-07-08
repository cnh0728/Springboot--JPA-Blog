package com.ramyun.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
//사용자가 요청 -> 응답(Data)

@RestController
public class HttpControllerTest {

    private final static String tag= "HttpControllerTest: "; 

    @GetMapping("/http/lombok")
    public String lombokTest(){
        Member m = new Member(1, "ramyun", "1234", "email");
        // Member m = Member.builder().username("username").password("password").email("email").build();
        System.out.println(tag + "getter: "+m.getId());
        m.setId(5000);
        System.out.println(tag + "setter: "+m.getId());
        return "lombok Test 완료";
    }

    //http://localhost:8080/http/get (select)
    @GetMapping("/http/get")
    public String getTest(Member m){
        return "get 요청: " + m.getId()+", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
    }

    //http://localhost:8080/http/post (insert)
    @PostMapping("/http/post")
    public String postTest(@RequestBody Member m){
        // return "post 요청: " + text;
       return "post 요청: " + m.getId()+", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
    }

    //http://localhost:8080/http/put (update)
    @PutMapping("/http/put")
    public String putTest(@RequestBody Member m){
        return "put 요청: " + m.getId()+", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
    }

    //http://localhost:8080/http/delete (delete)
    @DeleteMapping("/http/delete")
    public String deleteTest(){
        return "delete 요청";
    }


}
