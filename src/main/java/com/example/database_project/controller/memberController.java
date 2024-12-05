package com.example.database_project.controller;

import com.example.database_project.dto.MemberDTO;
import com.example.database_project.entity.MemberEntity;
import com.example.database_project.repository.MemberRepository;
import com.example.database_project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class memberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public memberController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/signUp")
    public String signUpPage() {
        return "/signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute MemberDTO memberDTO) {
        memberService.saveMember(memberDTO);
        return "/home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("memberDTO", loginResult);
            session.setAttribute("id", loginResult.getId());
            return "redirect:/home";
        }
        else{
            return "redirect:/login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }

}