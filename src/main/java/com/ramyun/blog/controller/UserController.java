package com.ramyun.blog.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramyun.blog.config.auth.PrincipalDetail;
import com.ramyun.blog.model.KakaoProfile;
import com.ramyun.blog.model.OAuthToken;
import com.ramyun.blog.model.User;
import com.ramyun.blog.service.UserService;

@Controller
public class UserController {
    
    @Value("${ramyun.key}")
    private String ramyunKey; 

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/auth/joinForm")
    public String joinForm(){
        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm(){
        return "user/loginForm";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallBack(String code) throws JsonMappingException, JsonProcessingException{ //get으로 날라오면 이렇게 바로 받을 수 있다.
//@ResponseBody 있으면 파일 안찾아감
        //POST방식으로 key=value 데이터를 요청 (카카오로)
        //Retrofit2, OkHttp, RestTemplate
        RestTemplate rt = new RestTemplate(); //post방식 쉽게함

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        //아래코드의 content-type은 key-value타입이라고 명시.
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("grant_type", "authorization_code");
        params.add("client_id","6f2035f47147666e09b1dec9fb2ace2f");
        params.add("redirect_uri","http://localhost:8000/auth/kakao/callback");
        params.add("code",code);

        //HttpHeader와 HttpBody를 하나의 오보젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest =
            new HttpEntity<>(params, headers);
        // Http요청하기 - Post방식, 그리고 response 변수의 응답을 받음
        ResponseEntity<String> response = rt.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);

        System.out.println("카카오 액세스 토큰: "+oauthToken.getAccess_token());

        RestTemplate rt2 = new RestTemplate(); //post방식 쉽게함

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
     
        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest2 =
            new HttpEntity<>(headers2);
        // Http요청하기 - Post방식, 그리고 response 변수의 응답을 받음
        ResponseEntity<String> response2 = rt2.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoTokenRequest2,
            String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();

        KakaoProfile kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);

        System.out.println("카카오 아이디(번호): "+kakaoProfile.getId());
        System.out.println("카카오 이메일: "+kakaoProfile.getKakao_account().getEmail());

        System.out.println("블로그서버 유저네임: "+kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
        System.out.println("블로그서버 이메일: "+kakaoProfile.getKakao_account().getEmail());
        //UUID란 중복되지 않은 어떤 특정값을 만들어내는 알고리듬
        System.out.println("블로그서버 패스워드: "+ramyunKey);

        User kakaoUser = User.builder()
        .username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
        .password(ramyunKey.toString())
        .email(kakaoProfile.getKakao_account().getEmail())
        .oauth("kakao")
        .build();
        
        //가입자 혹은 비가입자 체크해서 처리
        User originUser = userService.회원찾기(kakaoUser.getUsername());

        if(originUser.getUsername() == null){
            System.out.println("신규회원입니다....!");
            userService.회원가입(kakaoUser);
        }
        //로그인처리
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), ramyunKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }
    
    @GetMapping("/user/updateForm")
    public String updateForm(@AuthenticationPrincipal PrincipalDetail principal){
        return "user/updateForm";
    }

}
