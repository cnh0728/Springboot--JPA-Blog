package com.ramyun.blog.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ramyun.blog.model.RoleType;
import com.ramyun.blog.controller.dto.ReplySaveRequestDto;
import com.ramyun.blog.model.Board;
import com.ramyun.blog.model.Reply;
import com.ramyun.blog.model.User;
import com.ramyun.blog.repository.BoardRepository;
import com.ramyun.blog.repository.ReplyRepository;
import com.ramyun.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//스프링이 컴포턴트 스캔을 통해 Bean에 등록을 해줌 IoC를 해줌.
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void 글쓰기(Board board, User user){ //title,board
        board.setCount(0);
        board.setUser(user);
        boardRepository.save(board);
    }


    @Transactional(readOnly = true)
    public Page<Board> 글목록(Pageable pageable){
        return boardRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public Board 글상세보기(int id){
        return boardRepository.findById(id)
        .orElseThrow(()->{
            return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다.");
        });
    }

    @Transactional
    public void 글삭제하기(int id){
        boardRepository.deleteById(id);
    }

    @Transactional
    public void 글수정하기(int id, Board requestBoard){
        Board board = boardRepository.findById(id)
        .orElseThrow(()->{
            return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없습니다.");
        }); //영속화
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        //해당 함수로 종료시(service 종료될때) 트랜잭션이 종료. 이때 더티체킹-자동업데이트가 됨. db flush
    }

    @Transactional
    public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto){
        replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
    }

    @Transactional
    public void 댓글삭제(int replyId){
        replyRepository.deleteById(replyId);
    }

}
