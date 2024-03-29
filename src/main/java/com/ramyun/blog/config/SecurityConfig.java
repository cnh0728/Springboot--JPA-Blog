package com.ramyun.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ramyun.blog.config.auth.PrincipalDetailService;

//빈등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration //빈등록 (IoC관리)
@EnableWebSecurity // 시큐리티 필터 추가된다
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 저근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private PrincipalDetailService principalDetailService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder encodePWD(){
        return new BCryptPasswordEncoder();
    }

    //시큐리티가 대신 로그인 해주는데 password 가로채면
    //해당 password가 뭘로 해쉬 됐는지 알아야
    //같은 해쉬로 암호화해서 DB랑 비교할 수 있음.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            .csrf().disable() //csrf 토큰 비활성화 (테스트 시 걸기) 해킹관련 토큰임. 이게 있어야 접속가능
            .authorizeRequests()
                .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .formLogin()
                .loginPage("/auth/loginForm")
                .loginProcessingUrl("/auth/loginProc")
                .defaultSuccessUrl("/"); //스프링 시큐리티가 해당 주소로 요청 오는 로그인을 가로채서 대신 로그인 해준다.
    }
}
