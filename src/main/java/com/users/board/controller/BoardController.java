package com.users.board.controller;

import com.users.board.dto.BoardDTO;
import com.users.board.service.BoardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
// 중복되는 주소일 경우, @RequestMapping을 사용해 줄일 수 있다.
public class BoardController {

    private final BoardService boardService;

    // 글작성 페이지 이동
    @GetMapping("/save") // "/board/save"
    public String saveForm() {
        return "save";
    }

    // 글 작성 기능
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    // 전체 글 목록 조회
    @GetMapping("/")
    public String findAll(Model model) { // DB에서 데이터를 가지고 올때는 Model 객체를 사용한다.
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 출력한다.

        List<BoardDTO> boardDTOList = boardService.findAll();
        // 여러가지를 담아오기 위해선 List를 사용한다.

        model.addAttribute("boardList", boardDTOList);
        return "list";
    }

    // 특정 게시글 상세 조회
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        // @PathVariable 경로상에 존재하는 값을 받기 위해 사용.
        // 상세 조회시 고려해야할 사항
        // 1. 해당 게시글의 조회수를 1 올리고
        // 게시글 데이터를 가져와서 detail.html에 출력하기

        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "detail";
    }

    // 수정할 게시글 불러오기
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "update";
    }

    // 수정된 게시글 등록하기
    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "detail";
        // return "redirect:/board/" + boardDTO.getId();
        // 조회수 증가를 할 때 영향이 갈 수 있기 때문에 이렇게 쓸 수 있다 정도로만.
    }

    // 게시글 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }
}
