package com.users.board.entity;

import com.users.board.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Entity 클래스는 DB의 테이블 역할을 하는 클래스
@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity {
    @Id // pk 컬럼 지정. 반드시 존재해야하는 필수항목
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false) // 컬럼의 크기와 널 여부 지정. 여기서는 크기는 20, not null
    private String boardWriter;

    @Column // 만약 아무런 값을 주지 않으면, 크기 255, null 가능 이다.
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private int fileAttached; // 파일 존재 여부에 따라 1 또는 0으로 표시

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    //mappedBy은 BoardFileEntity의  "private BoardEntity boardEntity;" 로 맞춰주면 된다.
    // cascade = CascadeType.REMOVE, orphanRemoval = true. 이 두가지 속성은 ON DELETE CASCADE 속성을 준다.
    // BoardDTO 타입 객체를 BoardEntity 타입 객체로 바꿔주는 메서드
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();
    // 게시글 하나에 여러 파일을 올릴 수 있기 때문에 List<>를 사용한다.



    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); // 파일이 없는 경우 0으로 입력.
        return boardEntity;
    }

    // 수정된 게시글 등록하기
    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        boardEntity.setFileAttached(boardDTO.getFileAttached());
        return boardEntity;
    }

    public static BoardEntity toSaveFileEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(1); // 파일이 있기 때문에 1으로 입력.
        return boardEntity;
    }
}
