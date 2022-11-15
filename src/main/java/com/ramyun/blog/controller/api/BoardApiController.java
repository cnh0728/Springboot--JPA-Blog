package com.ramyun.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ramyun.blog.controller.dto.ReplySaveRequestDto;
import com.ramyun.blog.controller.dto.ResponseDto;
import com.ramyun.blog.model.RoleType;
import com.ramyun.blog.model.Board;
import com.ramyun.blog.model.Reply;
import com.ramyun.blog.service.UserService;
import com.ramyun.blog.service.BoardService;
import com.ramyun.blog.config.auth.PrincipalDetail;

@RestController
public class BoardApiController {

    @Autowired
    private BoardService boardService;
    

    @PostMapping("/api/board") 
    //얘가 최초로 실행됨. api/board로 들어오면 얘가 실행후 jsp 타고 들어가서
    //js로 들어가서 data를 api/board로 날림.  
    public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal){
        boardService.글쓰기(board, principal.getUser());
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id){
        boardService.글삭제하기(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board){
        boardService.글수정하기(id, board);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    //데이터를 받을 때 컨트롤러에서 dto를 만들어서 받는게 좋다.
    //dto 사용하지 않는 이유는 
    @PostMapping("/api/board/{boardId}/reply") 
    public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto ReplySaveRequestDto){

        boardService.댓글쓰기(ReplySaveRequestDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
    }

    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> replyDelete(@PathVariable int replyId){
        boardService.댓글삭제(replyId);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }
}
