package ogr.zerock.ex2.repository;

import ogr.zerock.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect(){
        Long mno = 100L;
        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("=============================");

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Test
    @Transactional
    public void testSelect2(){
        Long mno = 100L;
        Memo memo = memoRepository.getOne(mno);

        System.out.println("=============================");

        System.out.println(memo);
    }

    @Test
    public void testUpdate(){
        Memo memo = Memo.builder().mno(100L).memoText("Update Text222").build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete(){
        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault(){
        Pageable pageable = PageRequest.of(0,10);  // 1st - 찾을 페이지 번호(0부터 시작) , 2nd - 한 페이지의 레코드 수

        Page<Memo> result = memoRepository.findAll(pageable); // parameter -> Pageable I , Sort C

        System.out.println(result);

        System.out.println("--------------------------------");

        System.out.println("total pages : " + result.getTotalPages());  // 총 페이지 개수

        System.out.println("total counts : " + result.getTotalElements());  // 전체 레코드 개수

        System.out.println("page number : " + result.getNumber());  // 현재 페이지 번호 (0부터 시작)

        System.out.println("page size : " + result.getSize());  // 페이지당 데이터 개수

        System.out.println("has next page? : " + result.hasNext());  // 다음 페이지 존재 여부

        System.out.println("first page? : " + result.isFirst());  // 시작 페이지(0) 여부

        System.out.println("--------------------------------");

        for (Memo memo : result.getContent()){
            System.out.println(memo);
        }
    }

    @Test
    public void testSort(){
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);  // 여러 개의 정렬 조건을 and 로 결합하여 사용 가능

        Pageable pageable = PageRequest.of(0,10, sort1);
        Pageable pageable2 = PageRequest.of(0,10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);
        Page<Memo> result2 = memoRepository.findAll(pageable2);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });

        System.out.println("--------------------------------");

        for (Memo memo : result2.getContent()){
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethods(){
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for(Memo memo : list){
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> System.out.println(memo));
    }

    @Test
    @Commit  //  테스트 코드의 deleteBy는 기본적으로 롤백 처리되기 때문에 커밋하기 위한 어노테이션
    @Transactional // jpa 는 우선 select 문으로 해당 엔티티 객체를 가져오는 작업과 삭제하는 작업이 같이 이루어지기 때문에 있어야함!
    public void testDeleteQueryMethods(){
        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}
