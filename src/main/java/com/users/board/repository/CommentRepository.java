package com.users.board.repository;

import com.users.board.entity.BoardEntity;
import com.users.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    // 댓글 등록 후 전체 댓글 조회 기능
    // select * from comment_table where board_id = ? order by id desc;
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
