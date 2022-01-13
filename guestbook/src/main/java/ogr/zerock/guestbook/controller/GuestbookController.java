package ogr.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ogr.zerock.guestbook.dto.GuestbookDTO;
import ogr.zerock.guestbook.dto.PageRequestDTO;
import ogr.zerock.guestbook.service.GuestbookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service;

    @GetMapping("/")
    public String list(){
        log.info("list..........");

        return "/guestbook/list";
    }

    @GetMapping("/list")
    public void  list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list............" + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register(){
        log.info("register get....");
    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){ // redirectAttribute 이용해서 한 번만 화면에서 msg 이름의 변수 사용

        log.info("dto..." + dto);

        //새로 추가된 엔티티의 번호
        Long gno = service.register(dto);

        // addFlashAttribute() -> 단 한 번만 데이터를 전달하는 용도
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    // Get 방식으로 gno 값을 받아서 Model에 GuestbookDTO 객체를 담아서 전달
    // 나중에 다시 목록 페이지로 돌아가기 위해서 PageRequestDTO도 같이 저장
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("gno: " + gno);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto", dto);
    }

    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes) {
        log.info("gno: " + gno);

        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto,  // 수정해야하는 글의 정보를 가짐
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,   // 기존의 페이지 정보를 유지하기 위함
                         RedirectAttributes redirectAttributes) {   // 리다이렉트로 이동하기 위함
        log.info("post modify........");
        log.info("dto: " + dto);
        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("gno", dto.getGno());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());

        return "redirect:/guestbook/read";
    }
}
