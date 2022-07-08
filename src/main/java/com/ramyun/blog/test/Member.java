package com.ramyun.blog.test;

import lombok.AllArgsConstructor;
// import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    
    private int id;
    private String username;
    private String password;
    private String email;

    // @Builder //이거쓰면 생성자 편하다고 함. 
    // // Member m = Member.builder().username("username").password("password").email("email").build(); 이런식으로 순서 상관없이 짤 수 있다고 함.
    // public Member(int id, String username, String password, String email){
    //     this.id = id;
    //     this.username = username;
    //     this.password = password;
    //     this.email = email;
    // }    

}
