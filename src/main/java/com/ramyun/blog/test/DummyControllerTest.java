package com.ramyun.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ramyun.blog.model.RoleType;
import com.ramyun.blog.model.User;
import com.ramyun.blog.repository.UserRepository;


//html파일이 ㅇ ㅏ니라 data를 리턴해주는 controller = RestController
@RestController
public class DummyControllerTest {

    @Autowired //의존성 주입 
    //이게 붙으면 DummyControllerTest 가 메모리에 뜨면 UserRepository도 메모리에 뜸
    private UserRepository userRepository;

    @DeleteMapping("/dummy/user/{id}")
    public String delete(@PathVariable int id){
        try{
            userRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
            return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
        }

        return "삭제되었습니다. id: "+id;
    }    
    
    //save함수는 id를 전달하지 않으면 insert를 해주고
    //save함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update를 해주고
    //save함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert를 해줌.
    //email, password
    @Transactional
    @PutMapping("/dummy/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User requestUser){ //json 데이터를 요청 => Java Object(MessageCOnverter의 Jackson라이브러리가 변환해서 받아줘요.)
        System.out.println("id: "+id);
        System.out.println("password: "+requestUser.getPassword());
        System.out.println("email: "+requestUser.getEmail());

        User user = userRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("수정에 실패하였습니다.");
        });
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());

        
//        userRepository.save(user);
        //더티 체킹

        return user;
    }

    @GetMapping("/dummy/users")
    public List<User> list(){
        return userRepository.findAll();
    }

    //한페이지당 2건에 데이터를 리턴받을 예정
    @GetMapping("/dummy/user")
    public Page<User> pageList(@PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<User> pagingUser = userRepository.findAll(pageable);
        if(pagingUser.isLast()){
            //분기처리
        }
        List<User> users = pagingUser.getContent();
        return pagingUser;
    }

    //{id} 주소로 파라미터를 전달 받을 수 있음.
    //http://localhost:8000/blog/dummy/user/3
    @GetMapping("/dummy/user/{id}")
    public User detail(@PathVariable int id){
        //user/4를 찾으면 내가 데이터베이스에서 못찾아오면 null이니까
        //return null하면 프로그램에 문제가 생김.
        //Optional로 너의 User객체를 감싸서 가져올테니 그게 null인지 알아서 판단해
        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            //없으면 아래의 람다함수가 실행됨.
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 없습니다.");
            }
        });
        //람다식임
        // User user = userRepository.findById(id).orElseThrow(()->{
        //     return new IllegalArgumentException("해당 사용자는 없습니다.");
        // });

        //요청: 웹브라우저
        //user 객체 = 자바 오브젝트
        //변환 (웹브라우저가 이해할 수 있는 데이터) -> json (Gson 라이브러리)
        //스프링부트 = MessageConverter라는 애가 응답시에 자동 작동
        //만약에 자바 오브젝트를 리턴하게 되면 MessageConvereter가 Jackson 라이브러리를 호출해서
        //user 오브젝트를 json으로 변환해서 브라우저에 리턴하게 됨.
        return user;
    }

    //http://localhost:8000/blog/dummy/join (요청)
    //http의 body에 username, password, email 데이터를 가지고 (요청)
    @PostMapping("/dummy/join")
    // public String join(String username, String password, String email){ //key=value (약속된 규칙)
    public String join(User user){
        System.out.println("id: "+user.getId());
        System.out.println("username: "+user.getUsername());
        System.out.println("password: "+user.getPassword());
        System.out.println("email: "+user.getEmail());
        System.out.println("role: "+user.getRole());
        System.out.println("createDate: "+user.getCreateDate());

        user.setRole(RoleType.USER);
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }
}
