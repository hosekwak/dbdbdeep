package com.example.database_project.controller;

import com.example.database_project.dto.ListDTO;
import com.example.database_project.dto.MemberDTO;
import com.example.database_project.service.FavoriteService;
import com.example.database_project.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/list")
public class ListController {

    private final ListService listService;
    private final FavoriteService favoriteService;

    // 리스트 전체 조회
    @GetMapping
    public String list(
            @PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            HttpSession session
    ) {
        System.out.println("sortby result: " + session.getAttribute("sortby"));
        if(session.getAttribute("id") == null) {
            return "/home";
        }
        System.out.println("stop point: 40");
        int sortBy = (int) session.getAttribute("sortby");
        // sortBy 값에 따라 다른 정렬 방식을 처리
        Page<ListDTO> listPage = listService.paging(pageable);
        if (sortBy == 1) {
            listPage = listService.pagingsSortByLike(pageable); // 추천순 정렬
        }
        model.addAttribute("listPage", listPage);
        int currentPage = listPage.getNumber() + 1; // 페이지 번호는 0부터 시작하므로 1을 더함
        int totalPages = listPage.getTotalPages();
        System.out.println("stop point: 50");
        int startPage = ((currentPage - 1) / 10) * 10 + 1;
        int endPage = Math.min(startPage + 9, totalPages);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        System.out.println("stop point: 60");
        return "/list"; // 반환하는 뷰 이름
    }

    @GetMapping("/sortbylike")
    public String listSortByLike(
            Pageable pageable,
            Model model,
            HttpSession session)
    {
        session.setAttribute("sortby",(int)1);
        System.out.println("sortby result: " + session.getAttribute("sortby"));
        return "redirect:/list"; // 반환하는 뷰 이름
    }

    // 저장 폼 요청
    @GetMapping("/save")
    public String saveForm(HttpSession session) {
        System.out.println("session result: " + session.getAttribute("id"));
        System.out.println("session result: " + session.getAttribute("memberEmail"));
        System.out.println("session result: " + session.getAttribute("memberPassword"));
        return "/Lsave";
    }

    // 데이터 저장
    @PostMapping("/save")
    public String save(@ModelAttribute ListDTO listDTO, HttpSession session) {
        listService.save(listDTO, session);
        System.out.println("session result: " + session.getAttribute("id"));
        return "redirect:/list";
    }

    // 업데이트 폼 요청
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        ListDTO listDTO = listService.findBylID(id);
        model.addAttribute("list", listDTO);
        if((long) session.getAttribute("id") != listDTO.getMemberId()) {
            redirectAttributes.addFlashAttribute("alertMessage", "You are not authorized to delete this item!");
            return "redirect:/list";
        }
        else{
            return "/Lupdate";
        }

    }

    // 데이터 업데이트
    @PostMapping("/update")
    public String update(@ModelAttribute ListDTO listDTO, HttpSession session) {
        listService.update(listDTO, session);
        return "redirect:/list";
    }

    // 데이터 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        ListDTO listDTO = listService.findBylID(id);
        if((long) session.getAttribute("id") == listDTO.getMemberId()) {
            listService.deleteById(id);
        }
        else{
            redirectAttributes.addFlashAttribute("alertMessage", "You are not authorized to delete this item!");

        }
        return "redirect:/list";
    }

    @PostMapping("/like")
    public String increaseLike(@RequestParam Long LID, @RequestParam Long MID) {

        favoriteService.addFavorite(LID, MID);
        return "redirect:/list"; // 목록 페이지로 리다이렉트
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword,
                         @PageableDefault(size = 10, sort = "list_created_time", direction = Sort.Direction.DESC) Pageable pageable,
                         Model model) {
        Page<ListDTO> searchResults = listService.search(keyword, pageable);
        model.addAttribute("listPage", searchResults);
        model.addAttribute("keyword", keyword);
        return "/list";
    }



}
