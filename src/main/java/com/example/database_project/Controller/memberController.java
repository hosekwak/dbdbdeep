package com.example.database_project.Controller;

import com.example.database_project.dto.MemberDTO;
import com.example.database_project.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class memberController {
    private final MemberService memberService;

    public memberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signUp")
    public String signUpPage(@ModelAttribute MemberDTO memberDTO) {
        memberService.saveMember(memberDTO);
        return "/home";
    }

    @GetMapping("/signUp")
    public String signUp() {
        return "/signUp";
    }
}