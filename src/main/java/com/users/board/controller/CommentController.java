package com.users.board.controller;

import com.users.board.dto.CommentDTO;
import com.users.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment") // /comment로 시작하는 주소를 모두 받겠다는 의미
public class CommentController {

    private final CommentService commentService;

    // 댓글 저장 기능
    @PostMapping("/save") // detail.html의 commentWrite 함수 안에서 지정된 url.
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO) {
        // detail.html에서 넘어온 데이터를 DTO 객체에 담아 생성하려고 한다.
        System.out.println("commentDTO = " + commentDTO); // soutp
        Long saveResult = commentService.save(commentDTO);
        if (saveResult != null) {
            // 작성 성공 시,
            // 댓글이 작성되면 끝이 나는게 아니라, 기존 글에 댓글을 추가해 출력해야 한다.
            // 다시 전체 댓글을 가지고 와서 view에 반복문으로 출력해야 한다.
            // 따라서, 성공 시, 댓글 목록을 가져와서 리턴
            // 여기서 댓글 목록이란, 해당 게시글의 모든 댓글을 말한다.
            // 게시글의 아이디를 기준으로 잡으면 된다.
            // 댓글 등록 후 전체 댓글 조회 기능 ==============
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            // 여러개의 댓글을 가지고 오기 때문에 List<> 를 사용하고, 가지고 올 항목은 댓글이기 때문에
            // CommentDTO를 참조 타입으로 넣어준다.
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

    }
}
