package com.users.board.service;

import com.users.board.dto.BoardDTO;
import com.users.board.entity.BoardEntity;
import com.users.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO클래스 -> Entity 클래스
// Entity 클래스 -> DTO 클래스

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 글 작성 기능
    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    // 전체 글 목록 조회
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>(); // 리턴을 받을 객체를 만듦.

        //
        for (BoardEntity boardEntity : boardEntityList) {
            // 우선 BoardEntity 타입의 객체들을 BoardDTO 타입으로 바꿔준 다음 boardDTOList 안에 넣어준다.
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }

        return boardDTOList;
    }

    // 특정 게시글 상세 조회 시, 조회수 1 상승
    @Transactional // JPA에서 제공하는 메서드가 아닌 별도로 추가한 메서드의 경우 붙여주어야 한다.
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    // 특정 게시글 상세 조회 시, 아이디로 불러오기
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    // 수정된 게시글 등록하기
    public BoardDTO update(BoardDTO boardDTO) {
       BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        // save() 메서드는 insert와 update를 담당한다.
        // insert와 update를 구분하기 위해서는 파라미터 값으로 id가 있는지 없는지를 확인한다.
        // BoardEntity의 toSaveEntity() 메서드에서는 id가 없으므로 insert로 인식한다.

        return findById(boardDTO.getId());
    }

    // 게시글 삭제
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // 페이징 요청
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        // page = 몇 페이지를 볼 지,
        // int page 에서 -1을 하는 이유는, page 위치에 있는 값은 0부터 시작한다. 실제 사용자가 요청한 페이지에서 1개를 뺴야함.
        // pageLimit = 한 페이지에서 출력할 게시글의 개수.
        // Sort.by(...) = 정렬 기준을 잡음.
        // Sort.Direction.DESC = 내림차순 정렬
        // "id" = Entitiy의 id를 기준으로
        // 정리하면, 한 페이지당 게시글을 3개씩 보여주고 정렬 기준은 id 기준으로 내림차순 정렬.

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록에서 보여줄 내용 = id, writer, title, hits. createdTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getBoardCreatedTime()));
        // map() 메서드는 foreach 처럼 board 객체를 BoardDTO 객체로 하나씩 변환하는 기능을 가진다.

        return boardDTOS;
    }
}
