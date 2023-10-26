package com.users.board.service;

import com.users.board.dto.CommentDTO;
import com.users.board.entity.BoardEntity;
import com.users.board.entity.CommentEntity;
import com.users.board.repository.BoardRepository;
import com.users.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 저장 기능
    public Long save(CommentDTO commentDTO) {
        // 부모 엔티티(BOardEntity) 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            // DTO로 받아온 값을 Entity로 변환하는 작업이 필요하다.
            // 여기서 클래스 메서드를 사용하는 이유는,
            // 어떠한 Entity 생성자든 되도록이면 외부에 노출되어서는 안된다 라는 규칙이 있다.
            // Entity 자체가 Spring DATA JPA에서는 DB를 다루는 클래스 객체이기 때문이다.
            // 그래서 Entitiy를 보호 하는 의미에서 클래스 메서드만 사용한다.
            // CommentEntity commentEntity = new CommentEntity(); 이렇게 생성자를 만들지 말자는 의미이다.
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }


    }
    // 댓글 등록 후 전체 댓글 조회 기능
    public List<CommentDTO> findAll(Long boardId) {
        // select * from comment_table where board_id = ? order by id desc;
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        // EntityList -> DTOList로 변환 필요
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity: commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
