package ogr.zerock.guestbook.service;

import ogr.zerock.guestbook.dto.GuestbookDTO;
import ogr.zerock.guestbook.dto.PageRequestDTO;
import ogr.zerock.guestbook.dto.PageResultDTO;
import ogr.zerock.guestbook.entity.Guestbook;

public interface GuestbookService {
    Long register(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    GuestbookDTO read(Long gno);

    void remove(Long gno);

    void modify(GuestbookDTO dto);

    // java8 부터 추가된 인터페이스에서 실제 내용을 가지는 default 메서드. 기존 추상 클래스를 통해서 전달해야 하는 실제 코드를 인터페이스에 선언함으로서
    // 기존 인터페이스 -> 추상 클래스 -> 구현 클래스 구조에서 추상 클래스를 생략할 수 있음
    default Guestbook dtoToEntity(GuestbookDTO dto){
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default GuestbookDTO entityToDto(Guestbook entity){

        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }
}
