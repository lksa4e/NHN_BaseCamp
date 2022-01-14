package ogr.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ogr.zerock.ex2.entity.Memo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to); // Memo 객체의 mno 값이 xx 부터 xx 사이의 객체들을 구하고 mno 역순으로 정렬

    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable); // 위와 동일하나 정렬 정보를 입력받아서 처리

    void deleteMemoByMnoLessThan(Long num); // mno 가 num 보다 작은 데이터 삭제
    // deleteBy 는 sql 처럼 한번에 삭제하는게 아니라 mno = ? 를 반복하면서 각 엔티티 객체를 하나씩 삭제한다 -> 실제 개발에는 잘 사용되지 않음

    // mno 역순으로 정렬하는 query 어노테이션
    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc();

    // 파라미터 바인딩
    // ?1 ?2 와 같이 파라미터 순서를 이용하는 방식 (jdbc 의 PreparedStatement 와 비슷)
    // :xxx 와 같이 :파라미터이름 을 이용하는 방식
    // :#{} 와 같이 자바 빈 스타일을 이용하는 방식 (:가 복잡한 경우 객체를 사용할 때)
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno} ")
    int updateMemoText2(@Param("param") Memo memo);

    // @Query 를 사용한 페이징 처리와 정렬 처리 -> countQuery parameter + Page<T> 반환형
    @Query(value = "select m from Memo m where m.mno > :mno", countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);

    // Join or Group By 등을 사용하는 경우 적당한 엔티티 타입이 존재하지 않을 때 Object[] 형태로 받을 수 있음
    @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno",
    countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

    // Native Queryㅂ
    @Query(value = "select * from memo where mno > 0", nativeQuery = true)
    List<Object[]> getNativeResult();
}
