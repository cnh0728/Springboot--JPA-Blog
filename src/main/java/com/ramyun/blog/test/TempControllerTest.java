package com.ramyun.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {
    
    //http://localhost:8000/blog/temp/home
    @GetMapping("/temp/home")
    public String tempHome(){
        System.out.println("tempHome()");
        //파일 리턴 기본경로 : src/main/resources/static
        //리턴명 : home.html
        //풀경로 : src/main/resources/static/home.html 
        //(return에 /를 안붙이면 statichome.html이 돼버린다.)
        return "/home.html";
    }

    @GetMapping("/temp/img")
    public String tempImg(){
        return "/a.jpg";
    }

    @GetMapping("/temp/jsp")
    public String tempJsp(){
        // prefix: /WEB-INF/views/
        // suffix: .jsp
        // 풀네임 : /WEB-INF/views//test.jsp.jsp
        return "test";
    }
}
