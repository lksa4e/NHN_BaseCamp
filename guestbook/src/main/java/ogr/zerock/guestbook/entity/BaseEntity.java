package ogr.zerock.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass  // 해당 어노테이션이 적용된 클래스 -> 테이블로 생성되지 않는 클래스 -> 추상 클래스를 상속한 엔티티 클래스는 DB 테이블 생성
@EntityListeners(value = {AuditingEntityListener.class}) // Auditing.. -> JPA 내부에서 엔티티 객체의 변화 감지 -> 이를 통해 regDate, modDate 값 지정
@Getter
abstract class BaseEntity {

    @CreatedDate // JPA 에서 엔티티의 생성 시간을 처리
    @Column(name = "regdate", updatable = false) // updatable = false -> 엔티티 객체를 DB 반영할 때 regdate 칼럼 업데이트 x
    private LocalDateTime regDate;

    @LastModifiedDate // JPA 에서 최종 수정 시간을 자동으로 처리
    @Column(name ="moddate")
    private LocalDateTime modDate;
}
