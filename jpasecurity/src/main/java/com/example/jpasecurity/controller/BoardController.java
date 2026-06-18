package com.example.jpasecurity.controller;

import com.example.jpasecurity.dto.BoardDto;
import com.example.jpasecurity.entity.Board;
import com.example.jpasecurity.entity.FileAttach;
import com.example.jpasecurity.entity.JpaMember;
import com.example.jpasecurity.repository.FileAttachRepository;
import com.example.jpasecurity.service.BoardService;
import com.example.jpasecurity.service.UserAccount;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final FileAttachRepository fileAttachRepository;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String keyword,
                       Model model){

        Page<Board> boardPage = boardService.getList(page, keyword);


        //boardPage.getContent()
        //        .forEach(board-> log.info("{}", board));

        int totalPages = boardPage.getTotalPages(); //전체페이지
        int currentPage = boardPage.getNumber(); //현재 페이지
        int pageSize = 5;           // 한 번에 보여줄 페이지 버튼 수

        int startPage = (currentPage / pageSize) * pageSize;

        int endPage = Math.min(startPage + pageSize - 1, totalPages - 1);

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("keyword", keyword);

        return "board/list";

    }

    //게시글 작성 화면
    @GetMapping("/write")
    public String writeForm(Model model){
        model.addAttribute("boardDto", new BoardDto());
        return "board/write";
    }

    //게시글 등록 처리
    @PostMapping("write")
    public String write(@Valid @ModelAttribute("boardDto") BoardDto dto,
                        BindingResult result,
                        RedirectAttributes rttr,
                        @AuthenticationPrincipal UserAccount userAccount) throws Exception{

        if(result.hasErrors()) return "board/write";

        Board board = boardService.write(dto, userAccount.getJpaMember());

        rttr.addFlashAttribute("message", "게시글이 등록되었습니다.");

        return "redirect:/board/view/" + board.getId();
    }

    //게시글 상세 조회 - 댓글, 파일 목록도 함께 조회
    @GetMapping("view/{id}")
    public String view(@PathVariable Long id,
                       @AuthenticationPrincipal UserAccount userAccount,
                       Model model){

        Board board = boardService.getDetail(id); //조회수 증가 + 1
        //fileAttachRepository.findById(id) --> jpa_file_attach 테이블 기본키로 조회
        List<FileAttach> files = fileAttachRepository.findByBoardId(id);// jpa_file_attach 테이블 외래키(board_id)로 조회

        model.addAttribute("board", board);
//        model.addAttribute("comments", null);
        model.addAttribute("files", files);

        //현재 로그인 사용자 정보
        model.addAttribute("loginUser", userAccount.getJpaMember());

        return "board/view";
    } //end view

    // 게시글 수정 화면 - 본인만 접근 가능
    @GetMapping("edit/{id}")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal UserAccount userAccount,
                           Model model){

        Board board = boardService.findById(id);

        //본인 확인 — 작성자 id와 로그인 id 비교
        if(! board.getMember().getId().equals(userAccount.getJpaMember().getId())){
            return "redirect:/board/view" + id + "?error=borbidden";
        }

        BoardDto dto = BoardDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();

        model.addAttribute("board", board);
        model.addAttribute("boardDto", dto);

        return "board/edit";
    }

    // 게시글 수정 처리 — 본인만 처리
    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute("boardDto") BoardDto dto,
            BindingResult result,
            @AuthenticationPrincipal UserAccount userAccount,
            RedirectAttributes redirectAttributes) throws Exception {

        if (result.hasErrors()) return "board/edit";

        Board board = boardService.findById(id);

        if (!board.getMember().getId().equals(userAccount.getJpaMember().getId())) {
            return "redirect:/board/view/" + id + "?error=forbidden";
        }

        boardService.update(id, dto);

        redirectAttributes.addFlashAttribute("message", "수정이 완료되었습니다.");
        return "redirect:/board/view/" + id;
    }

    // 게시글 삭제 — 본인만 삭제 가능
    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserAccount userAccount,
            RedirectAttributes redirectAttributes) {

        Board board = boardService.findById(id);

        if (!board.getMember().getId().equals(userAccount.getJpaMember().getId())) {
            return "redirect:/board/view/" + id + "?error=forbidden";
        }

        boardService.delete(id);
        redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
        return "redirect:/board/list";
    }



}
