package ogr.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import ogr.zerock.guestbook.entity.Guestbook;
import ogr.zerock.guestbook.entity.QGuestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1,300).forEach(i -> {

            Guestbook guestbook = Guestbook.builder()
                    .title("Title...." + i)
                    .content("Content...." + i)
                    .writer("user" + (i % 10))
                    .build();

            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    public void updateTest(){

        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if(result.isPresent()){

            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title....");
            guestbook.changeContent("Changed Content....");

            // modDate는 최종 수정 시간이 반영되기 때문에 특정한 엔티티를 수정한 후 save() 했을 경우에 동작하게 됨
            guestbookRepository.save(guestbook);
        }
    }

    // 단일 항목 검색 테스트
    @Test
    public void testQuery1(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        // 동적으로 처리하기 위해서 Q 도메인 클래스를 얻어옴. Q 도메인 클래스를 이용하면 엔티티 클래스에 선언된 title, content 같은 필드들을 변수로 사용 가능
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        // BooleanBuilder -> where 문에 들어가는 조건들을 넣어주는 컨테이너
        BooleanBuilder builder = new BooleanBuilder();

        // 원하는 조건은 필드 값과 결합해서 생성. builder 안에 들어가는 값은 com.querydsl.core.types.Predicate 타입이어야 함
        BooleanExpression expression = qGuestbook.title.contains(keyword);

        // 만들어진 조건은 where 문에 and or 같은 키워드와 결합
        builder.and(expression);

        // BooleanBuilder 는 GuestbookRepository 에 추가된 QuerydslPredicateExcutor 인터페이스의 findAll()을 사용 가능
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }

    // 다중 항목 검색 테스트
    @Test
    public void testQuery2(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        // exTitle , exContent 결합
        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);

        // gno greater than
        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }

}
