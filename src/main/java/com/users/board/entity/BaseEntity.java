package com.users.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 시간을 다루는 별도의 Entity 클래스
// BoardEntity에 상속을 해주어 아래 기능들을 사용할 수 있게 된다.
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreationTimestamp // 무언가 생성이 됬을 때 시간 추가
    @Column(updatable = false) // 수정이 됬을 때는 시간 추가 X
    private LocalDateTime boardCreatedTime;

    @UpdateTimestamp // 업데이트 했을 때 시간 추가
    @Column(insertable = false) // 삽입이 되었을 때는 시간 추가 X
    private LocalDateTime updateTime;
}
