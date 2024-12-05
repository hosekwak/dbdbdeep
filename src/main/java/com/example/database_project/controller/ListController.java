package com.example.database_project.controller;

import com.example.database_project.dto.ListDTO;
import com.example.database_project.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/list")
public class ListController {

    private final ListService listService;

    // 리스트 전체 조회
    @GetMapping
    public String list(Model model) {
        // 서비스 계층에서 데이터 가져오기
        List<ListDTO> listList = listService.finALL();
        model.addAttribute("listList", listList);
        return "list";
    }

    // 저장 폼 요청
    @GetMapping("/save")
    public String saveForm() {
        return "Lsave";
    }

    // 데이터 저장
    @PostMapping("/save")
    public String save(@ModelAttribute ListDTO listDTO) throws IOException {
        listService.save(listDTO);
        return "redirect:/list";
    }

    // 업데이트 폼 요청
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable long id, Model model) {

        ListDTO listDTO = listService.findBylID(id);
        model.addAttribute("list", listDTO);
        return "Lupdate";
    }

    // 데이터 업데이트
    @PostMapping("/update")
    public String update(@ModelAttribute ListDTO listDTO) {
        listService.update(listDTO);
        return "redirect:/list";
    }

    // 데이터 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        listService.deleteById(id);
        return "redirect:/list";
    }
}
