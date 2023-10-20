package com.users.board.repository;

import com.users.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // JpaRepository<BoardEntity, Long>
    // JpaRepository<BoardEntity 타입, BoardEntity타입 안의 PK의 타입Long>

    // 조회수 증가 쿼리는 DB 기준으로,
    // update board_table set board_hits = board_hits + 1 where id = ?
    @Modifying // Update나 Delete 같은 쿼리문을 실행할 때 반드시 추가해야하는 어노테이션.
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits+1 where b.id = :id")
    // JPA에서 제공하는 어노테이션으로, 쿼리문을 작성할 수 있따.
    void updateHits(@Param("id") Long id);
}
