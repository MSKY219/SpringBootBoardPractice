package com.users.board.dto;

import com.users.board.entity.CommentEntity;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
// @Getter
// @Setter
// @ToString
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    // EntityList -> DTOList 변환
    public static CommentDTO toCommentDTO(CommentEntity commentEntity, Long boardId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getBoardCreatedTime());
        commentDTO.setBoardId(boardId);
        // commentDTO.setBoardId(commentEntity.getBoardEntity().getId()); 이렇게 사용하면,
        // 자식 엔티티에 있는 부모 엔티티 값에서 꺼내는 방법이다.
        // 대신  public static CommentDTO toCommendDTO(CommentEntity commentEntity) { ... } 와 같이 바꿔야한다.
        // 또한 Service 메서드에 @Transactional 을 추가해야 한다.
        return commentDTO;
    }
}
