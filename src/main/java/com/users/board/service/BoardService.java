package com.users.board.service;

import com.users.board.dto.BoardDTO;
import com.users.board.entity.BoardEntity;
import com.users.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
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
}
