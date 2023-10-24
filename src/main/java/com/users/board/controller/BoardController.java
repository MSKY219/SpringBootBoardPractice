package com.users.board.controller;

import com.users.board.dto.BoardDTO;
import com.users.board.service.BoardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
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
    public String findById(@PathVariable Long id, Model model, @PageableDefault(page=1) Pageable pageable) {
        // @PathVariable 경로상에 존재하는 값을 받기 위해 사용.
        // 상세 조회시 고려해야할 사항
        // 1. 해당 게시글의 조회수를 1 올리고
        // 게시글 데이터를 가져와서 detail.html에 출력하기

        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
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

    // 페이징 요청
    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1)Pageable pageable, Model model) {
        pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);

        int blockLimit = 3; // 실제 웹에서 보여질 페이징 수
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        // 현재 한 페이지에서 가장 앞에 위치할 3개의 페이지 수를 보여준다. 그 페이지 숫자 중, 가장 먼저 보여질 숫자는 1, 4, 7, 10 같이 3씩 커진다.
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();
        // 마지막 페이지는 3, 6, 9, 12 와 같이 나온다.
        // 하지만 페이지가 8까지만 있을 경우, 초과하는 값은 페이지 수에 포함하지 않는다.

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";
    }
}
